package com.rush.logistic.client.order_delivery.global.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String username;
    private UserRole role;
}
