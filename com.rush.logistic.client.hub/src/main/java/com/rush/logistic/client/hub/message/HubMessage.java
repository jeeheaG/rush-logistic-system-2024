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
    HUB_DELETED_SUCCESS("해당 Hub를 성공적으로 삭제하였습니다."),
    HUB_ALREADY_DELETED("해당 Hub는 이미 삭제되었습니다."),
    HUB_INFO_LIST_FOUND_SUCCESS("HUB 정보 리스트를 성공적으로 불러왔습니다."),
    HUB_INFO_LIST_NOT_FOUND("HUB 정보 리스트를 불러오는데 실패했습니다."),
    HUB_LIST_NOT_FOUND("HUB 리스트를 불러오는데 실패했습니다."),
    HUB_SAVE_FAILED("신규 HUB 저장에 실패했습니다."),
    HUB_ADDRESS_DUPLICATED("중복된 주소가 존재합니다."),
    HUB_NAME_DUPLICATED("중복된 이름이 존재합니다."),
    HUB_CREATE_FORBIDDEN("해당 권한으로는 HUB를 생성할 수 없습니다."),
    HUB_UPDATED_FORBIDDEN("해당 권한으로는 HUB를 수정할 수 없습니다."),
    HUB_DELETED_FORBIDDEN("해당 권한으로는 HUB를 삭제할 수 없습니다.");

    private final String message;
}
