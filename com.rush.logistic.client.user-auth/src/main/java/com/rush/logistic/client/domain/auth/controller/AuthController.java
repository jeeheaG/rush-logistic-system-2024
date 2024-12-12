package com.rush.logistic.client.domain.auth.controller;

import com.rush.logistic.client.domain.auth.dto.SignInRequestDto;
import com.rush.logistic.client.domain.auth.dto.SignUpRequestDto;
import com.rush.logistic.client.domain.auth.service.AuthService;
import com.rush.logistic.client.domain.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signIn")
    public ApiResponse<?> signIn(@RequestBody SignInRequestDto signInRequestDto){

        return ApiResponse.ok(authService.signIn(signInRequestDto.getUsername(), signInRequestDto.getPassword()));
    }

    @PostMapping("/auth/signUp")
    public ApiResponse<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        return ApiResponse.ok(authService.signUp(signUpRequestDto));
    }
}