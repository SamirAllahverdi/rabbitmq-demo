package com.example;

import com.example.entity.Employee;
import com.example.producer.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@SpringBootApplication
public class RabbitmqDemoApplication {

    private final Producer producer;

    @GetMapping
    public String produce() throws JsonProcessingException {
        producer.sendHello(new Employee(new Random().nextInt(), "Samir", LocalDate.now()));
        return "OK";
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }

}
