package com.rush.logistic.client.domain.user.controller;

import com.rush.logistic.client.domain.global.BaseResponseDTO;
import com.rush.logistic.client.domain.user.dto.UserInfoListResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.dto.UserUpdateRequestDto;
import com.rush.logistic.client.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    // TODO : gateway 헤더에서 userid랑 role 잘 넘겨받아서 master만 하게 하기
    //  그리고 authconfig에서 .requestMatchers("/api/users/**").permitAll() 이부분 수정하기
//    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @GetMapping("/users")
    public ResponseEntity<BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>>> getAllUsers(){

        BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>> responseDto = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // TODO : 일단 userId로 검색했는데 나중에 헤더에서 userId 받아서 본인것만 조회하게
    //  혹시 조회할때 마스터는 상관없게 구현?
    @GetMapping("/users/{userId}")
    public ResponseEntity<BaseResponseDTO<UserInfoResponseDto>> getUserById(
            @RequestHeader(value = "USER_ID", required = false) String authenticatedUserId,
            @PathVariable String userId) {

        // TODO : 임시
        authenticatedUserId = "1";

        // 요청한 사용자가 본인인지 확인
        if (!authenticatedUserId.equals(userId)) {
            BaseResponseDTO<UserInfoResponseDto> errorResponse = BaseResponseDTO.error("자기 자신의 데이터만 조회할 수 있습니다.", HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        BaseResponseDTO<UserInfoResponseDto> responseDto = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/users/{userId}")
    //    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<BaseResponseDTO<UserInfoResponseDto>> updateUser(@RequestBody UserUpdateRequestDto updateRequestDto, @PathVariable String userId) {

        BaseResponseDTO<UserInfoResponseDto> responseDto = userService.updateUser(userId, updateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/users/{userId}")
    //    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<BaseResponseDTO<UserInfoResponseDto>> deleteUser(@PathVariable String userId) {

        BaseResponseDTO<UserInfoResponseDto> responseDto = userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
