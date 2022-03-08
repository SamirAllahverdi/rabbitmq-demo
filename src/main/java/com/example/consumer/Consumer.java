package com.example.consumer;

import com.example.config.Config;
import com.example.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
@RequiredArgsConstructor
public class Consumer {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = Config.TEST_QUEUE, concurrency = "3-7")
    public void consume(Message message) throws JsonProcessingException {
        var json = new String(message.getBody());
        var employee = objectMapper.readValue(json, Employee.class);
        log.info("Consuming employee = {}, thread = {}", employee, Thread.currentThread().getName());
    }

}
