package com.books.recommendedservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration for RestTemplate - HTTP client for calling other microservices
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // Connection timeout - how long to wait for connection establishment
                .setConnectTimeout(Duration.ofSeconds(5))
                
                // Read timeout - how long to wait for response after connection
                .setReadTimeout(Duration.ofSeconds(10))
                
                .build();
    }
}