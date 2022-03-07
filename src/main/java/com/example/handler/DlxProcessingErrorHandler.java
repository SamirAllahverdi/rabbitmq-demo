package com.example.handler;

import com.example.header.RabbitmqHeader;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.time.LocalDateTime;


@Slf4j
public class DlxProcessingErrorHandler {


    @NonNull
    private final String deadExchangeName;

    private int maxRetryCount = 3;


    public DlxProcessingErrorHandler(String deadExchangeName) throws IllegalArgumentException {
        super();
        if (deadExchangeName.isEmpty()) {
            throw new IllegalArgumentException("Must define dlx exchange name");
        }
        this.deadExchangeName = deadExchangeName;
    }

    //override maxRetryCount
    public DlxProcessingErrorHandler(String deadExchangeName, int maxRetryCount) {
        this(deadExchangeName);
        setMaxRetryCount(maxRetryCount);
    }

    public void handleErrorProcessingMessage(Message message, Channel channel, long deliveryTag) {
        var rabbitMqHeader = new RabbitmqHeader(message.getMessageProperties().getHeaders());
        try {
            if (rabbitMqHeader.getFailedRetryCount() >= maxRetryCount) {
                log.warn("[DEAD] Error at " + LocalDateTime.now() + " on retry " + rabbitMqHeader.getFailedRetryCount()
                        + " for message " + new String(message.getBody()));
                channel.basicPublish(this.deadExchangeName, message.getMessageProperties().getReceivedRoutingKey(), null, message.getBody());
                channel.basicAck(deliveryTag, false);
            } else {
                log.warn("[REQUEUE] Error at " + LocalDateTime.now() + " on retry "
                        + rabbitMqHeader.getFailedRetryCount() + " for message " + new String(message.getBody()));
                channel.basicReject(deliveryTag, false);
            }
        } catch (IOException e) {
            log.warn("[HANDLER-FAILED] Error at " + LocalDateTime.now() + " on retry "
                    + rabbitMqHeader.getFailedRetryCount() + " for message " + new String(message.getBody()));
        }

    }

    public void setMaxRetryCount(int maxRetryCount) throws IllegalArgumentException {
        if (maxRetryCount > 1000) {
            throw new IllegalArgumentException("max retry must between 0-1000");
        }
        this.maxRetryCount = maxRetryCount;
    }

}
