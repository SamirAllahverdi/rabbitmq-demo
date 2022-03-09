package com.example.schedule;

import com.example.client.RabbitMQClient;
import com.example.entity.RabbitMQQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQScheduler {

    private final RabbitMQClient client;

//    @Scheduled(fixedDelay = 30 * 1000)
    public void sweepDirtyQueues() {
        try {
            var dirtyQueues = client.getAllQueues().stream().filter(RabbitMQQueue::isDirty).collect(Collectors.toList());
            dirtyQueues.forEach(q -> log.info("Queue {} has {} unprocessed messages", q.getName(), q.getMessages()));
        } catch (Exception e) {
            log.warn("Cannot sweep queue : {}", e.getMessage());
        }
    }

}
