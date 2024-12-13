package com.rush.logistic.client.order_delivery.domain.order.controller.client;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/users/{userId}")
    GetUserInfoRes getUserInfo(@PathVariable Long userId);
}
