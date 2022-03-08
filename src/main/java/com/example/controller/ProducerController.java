package com.example.controller;

import com.example.entity.Employee;
import com.example.entity.Picture;
import com.example.service.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping
    public String produce() throws JsonProcessingException {
        producerService.sendHello(new Employee(new Random().nextInt(), "Samir", LocalDate.now()));
        return "OK";
    }

    @GetMapping("/retry")
    public String retry() throws JsonProcessingException {
        producerService.sendRetry(new Picture("Mona Liza", "jpg", "news", 10000));
        return "OK";
    }


}
