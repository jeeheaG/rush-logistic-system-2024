package com.rush.logistic.client.domain.user.service;

import com.rush.logistic.client.domain.auth.dto.SignInResponseDto;
import com.rush.logistic.client.domain.global.BaseResponseDTO;
import com.rush.logistic.client.domain.user.dto.UserInfoListResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
    }

    public BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>> getAllUsers() {

        List<UserInfoResponseDto> userList = userRepository.findAll().stream().map(UserInfoResponseDto::new).collect(Collectors.toList());

        return  BaseResponseDTO
                .<UserInfoListResponseDto<UserInfoResponseDto>>success(UserInfoListResponseDto.of(userList));
    }
}
