package com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.DeliveryStatusEnum;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.CompanyResWrapper;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetStartEndHubIdOfCompanyRes;

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
    public Delivery toEntity(CompanyResWrapper<GetStartEndHubIdOfCompanyRes> startEndHubIdDto) { //Order order
        return Delivery.builder()
//                .order(order)
                .status(DeliveryStatusEnum.WAITING)
                .startHubId(startEndHubIdDto.result().departureHubId())
                .endHubId(startEndHubIdDto.result().arrivalHubId())
                .address(address)
                .receiverSlackId(receiverSlackId)
                .receiverId(receiverId)
                .build();
    }
}
