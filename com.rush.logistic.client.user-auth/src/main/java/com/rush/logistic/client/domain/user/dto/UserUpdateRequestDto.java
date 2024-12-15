package com.rush.logistic.client.domain.user.dto;

import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    private String username;
    private UserRoleEnum role;
    private String email;
    private UUID hubId;
    private UUID companyId;
    private UUID deliveryId;
}