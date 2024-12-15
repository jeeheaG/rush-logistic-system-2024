package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetStartEndHubIdOfCompanyReq(
        UUID departureCompanyId,
        UUID arrivalCompanyId
) {
    public static GetStartEndHubIdOfCompanyReq toDto(UUID startCompanyId, UUID endCompanyId) {
        return GetStartEndHubIdOfCompanyReq.builder()
                .departureCompanyId(startCompanyId)
                .arrivalCompanyId(endCompanyId)
                .build();
    }
}
