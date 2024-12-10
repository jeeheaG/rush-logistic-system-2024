package com.rush.logistic.client.domain.user.controller;

import com.rush.logistic.client.domain.global.BaseResponseDTO;
import com.rush.logistic.client.domain.user.dto.UserInfoListResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
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
    public ResponseEntity<BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>>> getAllUsers(@RequestHeader(value = "USER_ID", required = false) String userId){

        BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>> responseDto = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // TODO : 일단 username으로 검색했는데 나중에 헤더에서 userId 받아서 본인것만 조회하게
    @GetMapping("/users/{username}")
    public ResponseEntity<BaseResponseDTO<UserInfoResponseDto>> getUserById(
            @RequestHeader(value = "USER_ID", required = false) String authenticatedUserId,
            @PathVariable String username) {

        Long userId = userService.findByUsername(username).getUserId();
        //Long id = Long.parseLong(authenticatedUserId);

        // TODO : 임시
        Long id = 1L;

        // 요청한 사용자가 본인인지 확인
        if (!id.equals(userId)) {
            // error 메소드 사용
            BaseResponseDTO<UserInfoResponseDto> errorResponse = BaseResponseDTO.error("자기 자신의 데이터만 조회할 수 있습니다.", HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        // 단일 사용자 조회 로직
        BaseResponseDTO<UserInfoResponseDto> responseDto = userService.getUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
