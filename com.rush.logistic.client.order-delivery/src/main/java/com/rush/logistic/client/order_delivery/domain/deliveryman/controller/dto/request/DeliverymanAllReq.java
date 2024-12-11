package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliverymanAllReq(

) {
    public Deliveryman toEntity(Deliveryman deliveryman) {
        return Deliveryman.builder()

                .build();
    }
}
