package com.rush.logistic.client.domain.user.controller;

import com.rush.logistic.client.domain.global.ApiResponse;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.dto.UserUpdateRequestDto;
import com.rush.logistic.client.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ApiResponse<?> getAllUsers(@RequestHeader(value = "role", required = true) String role,
                                      @PageableDefault(page = 0, size = 10, sort = "createdAt",
                                              direction = Sort.Direction.DESC) Pageable pageable,
                                      @RequestParam Integer size){

        Page<UserInfoResponseDto> users = userService.getAllUsers(role, pageable, size);

        if (users.isEmpty()) {
            return ApiResponse.noContent();
        }

        return ApiResponse.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<?> getUserById(
            @RequestHeader(value = "USER_ID", required = true) String authenticatedUserId,
            @RequestHeader(value = "role", required = true) String role,
            @PathVariable String userId) {

        return ApiResponse.ok(userService.getUserById(role, userId, authenticatedUserId));
    }

    @PatchMapping("/users/{userId}")
    public ApiResponse<?> updateUser(@RequestHeader(value = "role", required = true) String role,
                                     @RequestBody UserUpdateRequestDto updateRequestDto,
                                     @PathVariable String userId) {

        return ApiResponse.ok(userService.updateUser(role, userId, updateRequestDto));
    }

    @DeleteMapping("/users/{userId}")
    public  ApiResponse<?> deleteUser(@RequestHeader(value = "role", required = true) String role,
                                      @PathVariable String userId) {

        return ApiResponse.ok(userService.deleteUser(role, userId));
    }
}
