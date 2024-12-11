package com.rush.logistic.client.order_delivery.domain.delivery.exception;

import com.rush.logistic.client.order_delivery.global.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeliveryCode implements StatusCode {
    //2xx
    CREATE_DELIVERY_OK(HttpStatus.CREATED, "배달 생성 성공"),
    UPDATE_DELIVERY_OK(HttpStatus.OK, "배달 수정 성공"),
    GET_DELIVERY_OK(HttpStatus.OK, "배달 단건 조회 성공"),
    SEARCH_DELIVERY_OK(HttpStatus.OK, "배달 검색 성공"),
    DELETE_DELIVERY_OK(HttpStatus.NO_CONTENT, "배달 삭제 성공"),

    //4xx
    DELIVERY_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 배달"),
    DELIVERY_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 배달"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
