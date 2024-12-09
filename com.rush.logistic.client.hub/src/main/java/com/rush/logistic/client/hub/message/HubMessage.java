package com.rush.logistic.client.hub.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HubMessage {
    HUB_CREATED_SUCCESS("신규 Hub가 성공적으로 생성되었습니다.");

    private final String message;
}
