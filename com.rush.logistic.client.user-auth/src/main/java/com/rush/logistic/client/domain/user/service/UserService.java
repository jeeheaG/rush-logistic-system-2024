package com.rush.logistic.client.domain.user.service;

import com.rush.logistic.client.domain.auth.dto.SignInResponseDto;
import com.rush.logistic.client.domain.auth.dto.SignUpResponseDto;
import com.rush.logistic.client.domain.global.BaseResponseDTO;
import com.rush.logistic.client.domain.user.dto.UserInfoListResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.dto.UserUpdateRequestDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
    }

    public BaseResponseDTO<UserInfoListResponseDto<UserInfoResponseDto>> getAllUsers() {

        List<UserInfoResponseDto> userList = userRepository.findAll().stream().map(UserInfoResponseDto::from).collect(Collectors.toList());

        return  BaseResponseDTO
                .success(UserInfoListResponseDto.of(userList));
    }

    public BaseResponseDTO<UserInfoResponseDto> getUserById(String userId) {

        Optional<User> user = userRepository.findById(Long.valueOf(userId));

        if (user.isEmpty()) {
            return BaseResponseDTO.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value());
        }

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.from(user.get());
        return BaseResponseDTO.success(userInfoResponseDto);
    }

    @Transactional(readOnly = false)
    public BaseResponseDTO<UserInfoResponseDto> updateUser(String userId, UserUpdateRequestDto updateRequestDto) {

        Optional<User> user = userRepository.findById(Long.valueOf(userId));

        if (user.isEmpty()) {
            return BaseResponseDTO.error("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value());
        }

        User getUser = user.get();
        getUser.updateUser(updateRequestDto);
        userRepository.save(getUser);

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.from(getUser);
        return BaseResponseDTO.success(userInfoResponseDto);
    }
}
