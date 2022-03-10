package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    public static final String TEST_QUEUE = "test";
    public static final String GUIDELINE_IMAGE_WORK_QUEUE = "q.guideline.image.work";
    public static final String GUIDELINE_DEAD_EXCHANGE = "x.guideline.dead";
    public static final String GUIDELINE_IMAGE_WORK_EXCHANGE = "x.guideline.work";
    public static final String SPRING_WORK_EXCHANGE = "x.spring.work";
    public static final String SPRING_IMAGE_WORK_QUEUE = "q.spring.image.work";
    public static final String SPRING_VECTOR_WORK_QUEUE = "q.spring.vector.work";

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().findAndAddModules().build();
    }

}
