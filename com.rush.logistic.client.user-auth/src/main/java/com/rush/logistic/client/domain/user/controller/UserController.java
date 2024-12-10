package com.rush.logistic.client.domain.user.controller;

import com.rush.logistic.client.domain.auth.dto.SignInRequestDto;
import com.rush.logistic.client.domain.auth.dto.SignInResponseDto;
import com.rush.logistic.client.domain.global.BaseResponseDTO;
import com.rush.logistic.client.domain.user.dto.UserInfoListResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoRequestDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    // TODO : 헤더에서 userid랑 role 잘 넘겨받아서 master만 하게 하기
    //  그리고 authconfig에서 .requestMatchers("/api/users/**").permitAll() 이부분 수정하기
//    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @GetMapping("/users")
    public ResponseEntity<BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>>> getAllUsers(@RequestHeader(value = "USER_ID", required = false) String userId){

        BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>> responseDto = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
