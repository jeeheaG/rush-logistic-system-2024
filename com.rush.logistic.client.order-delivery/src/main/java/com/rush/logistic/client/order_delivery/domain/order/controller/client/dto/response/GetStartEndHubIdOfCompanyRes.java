package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

import java.util.UUID;

public record GetStartEndHubIdOfCompanyRes(
        UUID departureHubId,
        UUID arrivalHubId
) {
}

