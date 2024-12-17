package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import java.util.UUID;

public record GetCompanyByIdRes(
        UUID hubId,
        String name,
        String address,
        String type
) {
}

