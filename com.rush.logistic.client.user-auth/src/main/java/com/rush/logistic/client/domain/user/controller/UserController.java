package com.rush.logistic.client.domain.user.controller;

import com.rush.logistic.client.domain.global.BaseResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoListResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.dto.UserUpdateRequestDto;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import com.rush.logistic.client.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<BaseResponseDto<UserInfoListResponseDto<UserInfoResponseDto>>> getAllUsers(@RequestHeader(value = "role", required = true) String role){

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponseDto.error("일치하지 않는 권한입니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        BaseResponseDto<UserInfoListResponseDto<UserInfoResponseDto>> responseDto = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<BaseResponseDto<UserInfoResponseDto>> getUserById(
            @RequestHeader(value = "USER_ID", required = true) String authenticatedUserId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable String userId) {

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){
            // 요청한 사용자가 본인인지 확인
            if (!authenticatedUserId.equals(userId)) {
                BaseResponseDto<UserInfoResponseDto> errorResponse = BaseResponseDto.error("자기 자신의 데이터만 조회할 수 있습니다.", HttpStatus.FORBIDDEN.value());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
        }

        BaseResponseDto<UserInfoResponseDto> responseDto = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<BaseResponseDto<UserInfoResponseDto>> updateUser(@RequestHeader(value = "role", required = true) String role,
                                                                           @RequestBody UserUpdateRequestDto updateRequestDto,
                                                                           @PathVariable String userId) {

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponseDto.error("일치하지 않는 권한입니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        BaseResponseDto<UserInfoResponseDto> responseDto = userService.updateUser(userId, updateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<BaseResponseDto<UserInfoResponseDto>> deleteUser(@RequestHeader(value = "role", required = true) String role,
                                                                           @PathVariable String userId) {

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponseDto.error("일치하지 않는 권한입니다.", HttpStatus.UNAUTHORIZED.value()));
        }
        BaseResponseDto<UserInfoResponseDto> responseDto = userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
