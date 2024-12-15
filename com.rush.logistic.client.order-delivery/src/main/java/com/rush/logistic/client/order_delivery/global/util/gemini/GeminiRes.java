package com.rush.logistic.client.order_delivery.global.util.gemini;

import lombok.Builder;

@Builder
public record GeminiRes(
        String message
) {
    public static GeminiRes toDto() {
        return GeminiRes.builder()
//                .message()
                .build();
    }
}
