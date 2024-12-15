package com.rush.logistic.client.domain.user.dto;

import com.rush.logistic.client.domain.user.entity.User;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long userId;
    private String username;
    private String password;
    private UserRoleEnum role;
    private String email;
    private UUID hubId;
    private UUID companyId;
    private UUID deliveryId;
    private LocalDateTime createAt;
    private String createdBy;
    private LocalDateTime updateAt;
    private String updatedBy;

    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .email(user.getEmail())
                .hubId(user.getHubId())
                .companyId(user.getCompanyId())
                .deliveryId(user.getDeliveryId())
                .createAt(LocalDateTime.now())
                .createdBy(user.getCreatedBy())
                .updateAt(LocalDateTime.now())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
}
