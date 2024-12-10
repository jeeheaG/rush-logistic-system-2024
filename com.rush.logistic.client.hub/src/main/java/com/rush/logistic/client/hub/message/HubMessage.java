package com.rush.logistic.client.hub.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HubMessage {
    HUB_CREATED_SUCCESS("신규 Hub가 성공적으로 생성되었습니다."),
    HUB_NOT_FOUND("해당 Hub를 찾을 수 없습니다."),
    HUB_FOUND_SUCCESS("해당 Hub를 성공적으로 찾았습니다."),
    HUB_UPDATED_SUCCESS("해당 Hub를 성공적으로 업데이트하였습니다."),
    HUB_INFO_LIST_FOUND_SUCCESS("HUB 정보 리스트를 성공적으로 불러왔습니다."),
    HUB_INFO_LIST_NOT_FOUND("HUB 정보 리스트를 불러오는데 실패했습니다.");

    private final String message;
}
