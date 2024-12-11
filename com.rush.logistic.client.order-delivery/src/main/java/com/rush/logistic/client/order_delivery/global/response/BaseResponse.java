package com.rush.logistic.client.order_delivery.global.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class BaseResponse {
    private final String status;
    private final String code;
    private final String message;
    private final Object data;


    public static BaseResponse toResponse(StatusCode statusCode) {
        return BaseResponse.builder()
                .status(String.valueOf(statusCode.getHttpStatus().value()))
                .code(statusCode.getHttpStatus().name())
                .message(statusCode.getMessage())
                .build();
    }

    public static BaseResponse toResponse(StatusCode statusCode, Object data) {
        return BaseResponse.builder()
                .status(String.valueOf(statusCode.getHttpStatus().value()))
                .code(statusCode.getHttpStatus().name())
                .message(statusCode.getMessage())
                .data(data)
                .build();
    }

    public static BaseResponse toResponse(HttpStatus status) {
        return BaseResponse.builder()
                .status(String.valueOf(status.value()))
                .code(status.name())
                .message(status.getReasonPhrase())
                .build();
    }

    public static BaseResponse toResponse(HttpStatus status, String message) {
        return BaseResponse.builder()
                .status(String.valueOf(status.value()))
                .code(status.name())
                .message(message)
                .build();
    }
}