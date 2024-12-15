package com.rush.logistic.client.hub.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HubRouteMessage {
    HUB_ROUTE_CREATED_SUCCESS("신규 Hub Route가 성공적으로 생성되었습니다."),
    HUB_ROUTE_NOT_CREATED("신규 Hub Route 생성에 실패했습니다."),
    HUB_ROUTE_NOT_FOUND("해당 Hub Route를 찾을 수 없습니다."),
    HUB_ROUTE_FOUND("해당 Hub Route를 찾았습니다."),
    HUB_ROUTE_UPDATED("Hub Route가 성공적으로 업데이트되었습니다."),
    HUB_ROUTE_NOT_UPDATED("Hub Route 업데이트에 실패했습니다."),
    HUB_ROUTE_ALREADY_DELETED("이미 삭제된 Hub Route 입니다."),
    HUB_ROUTE_DELETED_SUCCESS("해당 Hub Route를 성공적으로 삭제했습니다."),
    HUB_ROUTE_LIST_NOT_FOUND("Hub Route 목록을 불러오는데 실패했습니다."),
    HUB_ROUTE_INFO_LIST_FOUND("Hub Route 정보 목록을 성공적으로 불러왔습니다."),
    HUB_ROUTE_ALREADY_CREATED("이미 존재하는 Hub Route 입니다."),
    HUB_ROUTE_NOT_CONNECTED("연결되지 않은 HUB입니다.");

    private final String message;
}
