package com.rush.logistic.client.order_delivery.domain.deliveryman.exception;

import com.rush.logistic.client.order_delivery.global.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeliverymanCode implements StatusCode {
    //2xx
    CREATE_DELIVERYMAN_OK(HttpStatus.CREATED, "배송담당자 생성 성공"),
    UPDATE_DELIVERYMAN_OK(HttpStatus.OK, "배송담당자 수정 성공"),
    GET_DELIVERYMAN_OK(HttpStatus.OK, "배송담당자 단건 조회 성공"),
    SEARCH_DELIVERYMAN_OK(HttpStatus.OK, "배송담당자 검색 성공"),
    DELETE_DELIVERYMAN_OK(HttpStatus.NO_CONTENT, "배송담당자 삭제 성공"),

    //4xx
    DELIVERYMAN_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 배송담당자"),
    DELIVERYMAN_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 배송담당자"),
    AVAILABLE_DELIVERYMAN_NOT_EXIST(HttpStatus.CONFLICT, "현재 배정 가능한 배송담당자가 없음");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
