package com.rush.logistic.client.domain.user.service;

import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


//    @Transactional(readOnly = true)
//    public ResponseEntity<UserInfoResponseDto> getAllUsers(Integer size) {
//
//
//
//    }
}
