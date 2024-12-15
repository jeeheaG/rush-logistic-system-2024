package com.rush.logistic.client.company_product.global.client;

import com.rush.logistic.client.company_product.global.exception.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-auth")
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    Response<UserResponseDto> getUserById(
            @RequestHeader(value = "USER_ID") String authenticatedUserId,
            @RequestHeader(value = "role") String role,
            @PathVariable String userId);
}