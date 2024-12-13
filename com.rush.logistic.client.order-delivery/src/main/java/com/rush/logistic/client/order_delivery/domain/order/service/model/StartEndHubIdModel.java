package com.rush.logistic.client.order_delivery.domain.order.service.model;

import java.util.UUID;

public record StartEndHubIdModel(
        UUID startHubId,
        UUID endHubId
) {
}
