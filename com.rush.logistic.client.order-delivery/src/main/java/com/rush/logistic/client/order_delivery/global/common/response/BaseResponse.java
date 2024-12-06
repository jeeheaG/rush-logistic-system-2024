package com.rush.logistic.client.order_delivery.global.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseResponse {
    private final String status;
    private final String code;
    private final String message;
    private final Object data;


    public static BaseResponse toResponse(StatusCode statusCode) {
        return BaseResponse.builder()
                .code(statusCode.getHttpStatus().name())
                .message(statusCode.getMessage())
                .build();
    }

    public static BaseResponse toResponse(StatusCode statusCode, Object data) {
        return BaseResponse.builder()
                .code(statusCode.getHttpStatus().name())
                .message(statusCode.getMessage())
                .data(data)
                .build();
    }
}