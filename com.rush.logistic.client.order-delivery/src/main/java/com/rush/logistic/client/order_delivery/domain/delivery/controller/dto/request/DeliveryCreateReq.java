package com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.DeliveryStatusEnum;
import com.rush.logistic.client.order_delivery.domain.order.service.model.StartEndHubIdModel;

import java.util.UUID;

public record DeliveryCreateReq(
//        UUID orderId,
//        String status,
//        UUID startHubId,
//        UUID endHubId,
        String address,
        String receiverSlackId,
        UUID receiverId
) {
    public Delivery toEntity(StartEndHubIdModel startEndHubIdModel ) { //Order order
        return Delivery.builder()
//                .order(order)
                .status(DeliveryStatusEnum.WAITING)
                .startHubId(startEndHubIdModel.startHubId())
                .endHubId(startEndHubIdModel.endHubId())
                .address(address)
                .receiverSlackId(receiverSlackId)
                .receiverId(receiverId)
                .build();
    }
}
