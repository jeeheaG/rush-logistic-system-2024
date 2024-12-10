package com.rush.logistic.client.domain.auth.dto;

import com.rush.logistic.client.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {

    private String username;
    private String slackId;

    public static SignUpResponseDto of(User user) {
        return SignUpResponseDto.builder()
                .username(user.getUsername())
                .slackId(user.getSlackId())
                .build();
    }
}
