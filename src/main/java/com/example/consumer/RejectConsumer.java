package com.example.consumer;


import com.example.config.Config;
import com.example.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
@Slf4j
@RequiredArgsConstructor
public class RejectConsumer {

    private final ObjectMapper objectMapper;

    // Automatic Rejection
    @RabbitListener(queues = Config.TEST_QUEUE)
    public void listen(String message) throws IOException {
        var employee = objectMapper.readValue(message, Employee.class);
        if (employee.getId() > 9000) {
			throw new AmqpRejectAndDontRequeueException("Wrong employee id" + employee.getId());
        }
        log.info("Consuming employee = {}", employee);
    }

    // Manual Rejection
    @RabbitListener(queues = Config.TEST_QUEUE)
    public void listen(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        var employee = objectMapper.readValue(message, Employee.class);

        if (employee.getId() > 9000) {
            channel.basicReject(tag, false);
            return;
        }

        log.info("Consuming employee = {}", employee);
        channel.basicAck(tag, false);
    }
}
