package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record SendSlackMessageRes(
        String id,
        String message,
        String sendUserId,
        String receiveUserSlackId,
        String createdAt,
        String createdBy,
        String updatedAt,
        String updatedBy
) {
}
