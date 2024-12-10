package com.rush.logistic.client.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    MASTER("MASTER"),
    HUB("HUB"),
    DELIVERY("DELIVERY"),
    COMPANY("COMPANY");

    private final String role;
}
