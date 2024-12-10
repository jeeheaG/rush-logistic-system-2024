package com.rush.logistic.client.domain.global;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponseDTO<T> {
    private int statusCode;
    private String message;
    private T data;

    public static <T> BaseResponseDTO<T> success(T data) {
        return BaseResponseDTO.<T>builder()
                .statusCode(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> BaseResponseDTO<T> error(String message, int statusCode) {
        return BaseResponseDTO.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}