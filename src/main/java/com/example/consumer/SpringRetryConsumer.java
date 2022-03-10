package com.example.consumer;

import com.example.config.Config;
import com.example.entity.Picture;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

//@Component
@Slf4j
@RequiredArgsConstructor
public class SpringRetryConsumer {

    // For use the consumer, uncomment retry fields in app.yaml
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = Config.SPRING_IMAGE_WORK_QUEUE)
    public void listenImage(String message) throws IOException {
        var picture = objectMapper.readValue(message, Picture.class);
        log.info("Consuming image {}", picture.getName());

        if (picture.getSize() > 9000) {
            throw new IllegalArgumentException("Image too large : " + picture.getName());
        }

        log.info("Processing image : " + picture.getName());
    }

    @RabbitListener(queues = Config.SPRING_VECTOR_WORK_QUEUE)
    public void listenVector(String message) throws IOException {
        var picture = objectMapper.readValue(message, Picture.class);
        log.info("Consuming vector {}", picture.getName());
        log.info("Processing vector : " + picture.getName());
    }

}
