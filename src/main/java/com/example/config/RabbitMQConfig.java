package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String TEST_QUEUE = "test";

    @Bean
    public ObjectMapper objectMapper() {
       return JsonMapper.builder().findAndAddModules().build();
    }

}
