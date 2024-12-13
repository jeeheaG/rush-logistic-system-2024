package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HubRouteInfoReq(
        UUID startHubId,
        UUID endHubId
) {

}
