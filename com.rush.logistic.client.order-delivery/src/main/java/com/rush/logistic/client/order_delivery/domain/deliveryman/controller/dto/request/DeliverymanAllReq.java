package com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request;

import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import lombok.Builder;


@Builder
public record DeliverymanAllReq(

) {
    public Deliveryman toEntity(Deliveryman deliveryman) {
        return Deliveryman.builder()

                .build();
    }
}
