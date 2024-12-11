package com.rush.logistic.client.domain.auth.controller;

import com.rush.logistic.client.domain.auth.dto.SignInRequestDto;
import com.rush.logistic.client.domain.auth.dto.SignInResponseDto;
import com.rush.logistic.client.domain.auth.dto.SignUpRequestDto;
import com.rush.logistic.client.domain.auth.dto.SignUpResponseDto;
import com.rush.logistic.client.domain.auth.service.AuthService;
import com.rush.logistic.client.domain.global.BaseResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/auth/signIn")
    public ResponseEntity<BaseResponseDto<SignInResponseDto>> signIn(@RequestBody SignInRequestDto signInRequestDto){

        User user = userService.findByUsername(signInRequestDto.getUsername());
        BaseResponseDto<SignInResponseDto> responseDto = authService.signIn(user.getUserId(), signInRequestDto.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<BaseResponseDto<SignUpResponseDto>> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        BaseResponseDto<SignUpResponseDto> responseDto = authService.signUp(signUpRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


}