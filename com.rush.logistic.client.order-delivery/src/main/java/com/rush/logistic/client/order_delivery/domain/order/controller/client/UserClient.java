package com.rush.logistic.client.order_delivery.domain.order.controller.client;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.UserSlackResWrapper;
import com.rush.logistic.client.order_delivery.global.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-auth", configuration = FeignConfig.class) // user-service
public interface UserClient {
    @GetMapping("/api/users/{userId}")
    UserSlackResWrapper<GetUserInfoRes> getUserInfo(@PathVariable Long userId);
}
