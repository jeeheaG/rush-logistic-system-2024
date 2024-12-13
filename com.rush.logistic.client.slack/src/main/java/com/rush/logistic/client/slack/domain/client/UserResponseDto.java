package com.rush.logistic.client.slack.domain.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String userId;
    private String username;
    private String password;
    private String slackId;
    private String role;
    private String email;
}