package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request;

import lombok.Builder;

@Builder
public record SendSlackMessageReq(
        String message,
        String email
) {
    public static SendSlackMessageReq toDto(
            String message,
            String email
    ) {
        return SendSlackMessageReq.builder()
                .message(message)
                .email(email)
                .build();
    }
}
