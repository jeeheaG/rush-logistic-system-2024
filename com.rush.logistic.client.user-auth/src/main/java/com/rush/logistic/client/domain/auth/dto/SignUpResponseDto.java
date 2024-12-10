package com.rush.logistic.client.domain.auth.dto;

import com.rush.logistic.client.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponseDto {

    private String username;
    private String slackId;

    public static SignUpResponseDto from(User user) {
        return SignUpResponseDto.builder()
                .username(user.getUsername())
                .slackId(user.getSlackId())
                .build();
    }
}
