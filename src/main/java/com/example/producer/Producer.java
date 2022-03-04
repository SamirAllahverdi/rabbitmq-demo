package com.example.producer;

import com.example.config.Config;
import com.example.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public void sendHello(Employee employee) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(employee);
        rabbitTemplate.convertAndSend(Config.TEST_QUEUE, json);
    }

}
