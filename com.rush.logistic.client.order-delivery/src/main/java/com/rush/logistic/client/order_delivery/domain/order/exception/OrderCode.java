package com.rush.logistic.client.order_delivery.domain.order.exception;

import com.rush.logistic.client.order_delivery.global.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderCode implements StatusCode {
    //2xx
    CREATE_ORDER_OK(HttpStatus.CREATED, "주문 생성 성공"),
    UPDATE_ORDER_OK(HttpStatus.OK, "주문 수정 성공"),
    GET_ORDER_OK(HttpStatus.OK, "주문 단건 조회 성공"),
    SEARCH_ORDER_OK(HttpStatus.OK, "주문 검색 성공"),
    DELETE_ORDER_OK(HttpStatus.NO_CONTENT, "주문 삭제 성공"),

    //4xx
    ORDER_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 주문"),
    ORDER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 주문"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
