package com.rush.logistic.client.domain.user.dto;

import com.rush.logistic.client.domain.auth.dto.SignInResponseDto;
import com.rush.logistic.client.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {

    private User user;

    public static UserInfoResponseDto of() {
        return UserInfoResponseDto.builder()
                .user(User.builder().build())
                .build();
    }
}
