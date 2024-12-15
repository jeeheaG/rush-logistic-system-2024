package com.rush.logistic.client.domain.user.repository;

import com.rush.logistic.client.domain.user.dto.UserInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UserInfoResponseDto> findAll(Pageable pageable, int size);
}
