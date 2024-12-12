package com.rush.logistic.client.slack.domain.client;

import com.rush.logistic.client.domain.global.BaseResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-auth")
public interface UserClient {

    @GetMapping("/users/{userId}")
    ResponseEntity<BaseResponseDto<UserInfoResponseDto>> getUser(@PathVariable("id") String id);

}
