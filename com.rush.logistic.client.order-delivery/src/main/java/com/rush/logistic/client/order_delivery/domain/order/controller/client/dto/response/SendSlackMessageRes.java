package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record SendSlackMessageRes(
        String slackId,
        String message,
        ZonedDateTime createdAt,
        String sendUserId,
        String receiveUserId
) {
}
