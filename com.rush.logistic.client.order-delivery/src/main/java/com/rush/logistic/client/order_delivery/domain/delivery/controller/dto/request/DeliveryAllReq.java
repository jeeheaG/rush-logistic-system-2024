package com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.DeliveryStatusEnum;
import java.util.UUID;

public record DeliveryAllReq(
//        UUID orderId,
        String status,
        UUID startHubId,
        UUID endHubId,
        String address,
        String receiverSlackId,
        UUID receiverId
) {
    public Delivery toEntity() { //Order order
        return Delivery.builder()
//                .order(order)
                .status(DeliveryStatusEnum.valueOf(status))
                .startHubId(startHubId)
                .endHubId(endHubId)
                .address(address)
                .receiverSlackId(receiverSlackId)
                .receiverId(receiverId)
                .build();
    }
}
