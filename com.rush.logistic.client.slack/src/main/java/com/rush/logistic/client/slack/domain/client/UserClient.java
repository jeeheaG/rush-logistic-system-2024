package com.rush.logistic.client.slack.domain.client;

import com.rush.logistic.client.slack.domain.global.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "user-auth")
public interface UserClient {

    @GetMapping("/users/{userId}")
    ApiResponse<UserResponseDto> getUserById(
            @RequestHeader(value = "USER_ID") String authenticatedUserId,
            @RequestHeader(value = "role") String role,
            @PathVariable String userId);
}
