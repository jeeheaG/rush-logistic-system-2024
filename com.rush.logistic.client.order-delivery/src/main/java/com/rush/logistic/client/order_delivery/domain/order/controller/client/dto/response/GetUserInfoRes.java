package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import com.rush.logistic.client.order_delivery.global.auth.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record GetUserInfoRes(
        Long userId,
        String username,
        String password,
        UserRole role,
        String email,
        UUID hubId,
        UUID companyId
//        UUID deliveryId
) {
}
