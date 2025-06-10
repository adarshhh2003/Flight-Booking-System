package com.search.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientInterceptor {

    private static final String INTERNAL_TOKEN = "INTERNAL_SECRET_456";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("X-Internal-Token", INTERNAL_TOKEN);
    }
}
