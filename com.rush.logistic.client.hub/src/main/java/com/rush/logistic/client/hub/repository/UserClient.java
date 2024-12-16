package com.rush.logistic.client.hub.repository;

import com.rush.logistic.client.hub.dto.BaseResponseDto;
import com.rush.logistic.client.hub.dto.UserDto;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="user-auth")
public interface UserClient {
    @GetMapping("/api/users/{userId}")
    BaseResponseDto<UserDto> getUserById(@RequestHeader(value = "USER_ID", required = false) Long id,
                                         @RequestHeader(value = "role") String role,
                                         @PathVariable Long userId);
}
