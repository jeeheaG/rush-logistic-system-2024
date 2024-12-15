package com.rush.logistic.client.order_delivery.global.auth.checker;

import com.rush.logistic.client.order_delivery.global.auth.UserRole;

public record RoleOption(
        UserRole role,
        Boolean checkIsInCharge
) {
}
