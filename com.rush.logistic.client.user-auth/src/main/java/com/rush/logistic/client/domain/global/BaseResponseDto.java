package com.rush.logistic.client.domain.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponseDto<T> {
    private int statusCode;
    private String message;
    private T data;

    public static <T> BaseResponseDto<T> success(T data) {
        return BaseResponseDto.<T>builder()
                .statusCode(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> BaseResponseDto<T> error(String message, int statusCode) {
        return BaseResponseDto.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}