package com.rush.logistic.client.hub.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HubRouteMessage {
    HUB_ROUTE_CREATED_SUCCESS("신규 Hub Route가 성공적으로 생성되었습니다."),
    HUB_ROUTE_NOT_CREATED("신규 Hub Route 생성에 실패했습니다."),
    HUB_ROUTE_NOT_FOUND("해당 Hub Route를 찾을 수 없습니다."),
    HUB_ROUTE_FOUND("해당 Hub Route를 찾았습니다.");

    private final String message;
}
