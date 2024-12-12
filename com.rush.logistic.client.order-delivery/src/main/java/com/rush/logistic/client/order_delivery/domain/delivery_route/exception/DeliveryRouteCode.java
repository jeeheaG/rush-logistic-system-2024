package com.rush.logistic.client.order_delivery.domain.delivery_route.exception;

import com.rush.logistic.client.order_delivery.global.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeliveryRouteCode implements StatusCode {
    //2xx
    CREATE_DELIVERY_ROUTE_OK(HttpStatus.CREATED, "배송 경로 생성 성공"),
    UPDATE_DELIVERY_ROUTE_OK(HttpStatus.OK, "배송 경로 수정 성공"),
    GET_DELIVERY_ROUTE_OK(HttpStatus.OK, "배송 경로 단건 조회 성공"),
    SEARCH_DELIVERY_ROUTE_OK(HttpStatus.OK, "배송 경로 검색 성공"),
    DELETE_DELIVERY_ROUTE_OK(HttpStatus.NO_CONTENT, "배송 경로 삭제 성공"),

    //4xx
    DELIVERY_ROUTE_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 배송 경로"),
    DELIVERY_ROUTE_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 배송 경로"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
