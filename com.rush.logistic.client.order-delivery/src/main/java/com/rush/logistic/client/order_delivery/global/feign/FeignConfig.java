package com.rush.logistic.client.order_delivery.global.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public AuthHeaderInterceptor feignInterceptor() {
        return new AuthHeaderInterceptor();
    }
}
