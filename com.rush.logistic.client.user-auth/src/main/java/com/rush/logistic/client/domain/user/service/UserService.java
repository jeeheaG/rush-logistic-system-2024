package com.rush.logistic.client.domain.user.service;

import com.rush.logistic.client.domain.global.ApiResponse;
import com.rush.logistic.client.domain.global.exception.user.FailedLoginException;
import com.rush.logistic.client.domain.global.exception.user.NoAuthorizationException;
import com.rush.logistic.client.domain.global.exception.user.NotFoundUserException;
import com.rush.logistic.client.domain.user.dto.UserInfoListResponseDto;
import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.dto.UserUpdateRequestDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import com.rush.logistic.client.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    public Page<UserInfoResponseDto> getAllUsers(String role, Pageable pageable, Integer size) {

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){
            throw new NoAuthorizationException();
        }

        return userRepository.findAll(pageable).map(UserInfoResponseDto::from);
    }

    public UserInfoResponseDto getUserById(String role, String userId, String authenticatedUserId) {

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){
            if (!authenticatedUserId.equals(userId)) {
                throw new NoAuthorizationException();
            }
        }

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(NotFoundUserException::new);

        return UserInfoResponseDto.from(user);
    }

    @Transactional(readOnly = false)
    public UserInfoResponseDto updateUser(String role, String userId, UserUpdateRequestDto updateRequestDto) {

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){
            throw new NoAuthorizationException();
        }

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(NotFoundUserException::new);

        user.updateUser(updateRequestDto);
        userRepository.save(user);

        return UserInfoResponseDto.from(user);
    }

    @Transactional(readOnly = false)
    public UserInfoResponseDto deleteUser(String role, String userId) {

        if(!Objects.equals(role, UserRoleEnum.MASTER.name())){
            throw new NoAuthorizationException();
        }

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(NotFoundUserException::new);

        user.setDelete(true);
        userRepository.save(user);

        return UserInfoResponseDto.from(user);
    }
}
