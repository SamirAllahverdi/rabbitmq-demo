package com.example.consumer;

import com.example.config.Config;
import com.example.entity.Picture;
import com.example.handler.DlxProcessingErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RetryImageConsumer {


    private final DlxProcessingErrorHandler dlxProcessingErrorHandler;
    private final ObjectMapper objectMapper;

    public RetryImageConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.dlxProcessingErrorHandler = new DlxProcessingErrorHandler(Config.GUIDELINE_DEAD_EXCHANGE);
    }

    @RabbitListener(queues = Config.GUIDELINE_IMAGE_WORK_QUEUE, ackMode = "MANUAL")
    public void listen(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            var p = objectMapper.readValue(message.getBody(), Picture.class);
            if (p.getSize() > 9000) {
                throw new IOException("Size too large");
            } else {
                log.info("Creating thumbnail & publishing : " + p);
                log.info(String.valueOf(deliveryTag));
                channel.basicAck(deliveryTag, false);
            }
        } catch (IOException e) {
            log.warn("Error processing message : " + new String(message.getBody()) + " : " + e.getMessage());
            dlxProcessingErrorHandler.handleErrorProcessingMessage(message, channel, deliveryTag);
        }
    }

}
