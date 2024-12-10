package com.rush.logistic.client.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponseDto {

    private String access_token;

    public static SignInResponseDto from(String access_token) {
        return SignInResponseDto.builder()
                .access_token(access_token)
                .build();
    }
}
