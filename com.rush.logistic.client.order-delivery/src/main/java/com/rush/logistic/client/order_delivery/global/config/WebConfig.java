package com.rush.logistic.client.order_delivery.global.config;

import com.rush.logistic.client.order_delivery.global.auth.AuthArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthArgumentResolver authArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);

//        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}