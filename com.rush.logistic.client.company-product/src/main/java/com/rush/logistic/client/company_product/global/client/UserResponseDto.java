package com.rush.logistic.client.company_product.global.client;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {

    private String userId;
    private String username;
    private String password;
    private String slackId;
    private String role;
    private String email;
    private String hubId;
    private String companyId;
    private String deliveryId;
}