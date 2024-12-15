package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import lombok.Builder;

@Builder
public record GetUserInfoRes(
        Long userId,
        String username,
        String password,
        UserRole role,
        String email
) {
}
