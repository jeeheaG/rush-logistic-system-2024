package com.rush.logistic.client.domain.user.dto;

import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    private String username;
    private String slackId;
    private UserRoleEnum role;
    private String email;
}