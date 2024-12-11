package com.rush.logistic.client.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDto {

    private String access_token;

    public static SignInResponseDto of(String access_token) {
        return SignInResponseDto.builder()
                .access_token(access_token)
                .build();
    }
}
