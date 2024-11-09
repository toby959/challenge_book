package com.toby959.challenge.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;


@Configuration
//@ComponentScan(basePackages = "com.toby959.challenge")
public class AppConfig {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
}


