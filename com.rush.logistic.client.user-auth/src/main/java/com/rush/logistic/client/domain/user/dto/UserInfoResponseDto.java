package com.rush.logistic.client.domain.user.dto;

import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long userId;
    private String username;
    private String password;
    private String slackId;
    private UserRoleEnum role;

    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .slackId(user.getSlackId())
                .role(user.getRole())
                .build();
    }
}
