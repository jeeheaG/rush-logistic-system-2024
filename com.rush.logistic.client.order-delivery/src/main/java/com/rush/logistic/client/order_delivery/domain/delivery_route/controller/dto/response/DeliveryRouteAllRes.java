package com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record DeliveryRouteAllRes(
        UUID id,
//        DeliverymanAllRes deliveryman,
        Integer sequence,
        DeliveryRouteStatusEnum status,
//        DeliveryAllRes delivery,
        UUID startHubId,
        UUID endHubId,
        Integer expectedDistance,
        Integer expectedTime,
        Integer realDistance,
        Integer realTime,
        DeliveryTypeEnum type,

        String createdBy,
        ZonedDateTime createdAt,
        String updatedBy,
        ZonedDateTime updatedAt,
        String deletedBy,
        ZonedDateTime deletedAt,
        Boolean isDelete
) {
    public static DeliveryRouteAllRes fromEntity(DeliveryRoute deliveryRoute) {
//        DeliverymanAllRes deliverymanAllRes = DeliverymanAllRes.fromEntity(deliveryRoute.getDeliveryman());
//        DeliveryAllRes deliveryAllRes = DeliveryAllRes.fromEntity(deliveryRoute.getDelivery());

        return DeliveryRouteAllRes.builder()
                .id(deliveryRoute.getId())
//                .deliveryman(deliverymanAllRes)
                .sequence(deliveryRoute.getSequence())
                .status(deliveryRoute.getStatus())
//                .delivery(deliveryAllRes)
                .startHubId(deliveryRoute.getStartHubId())
                .endHubId(deliveryRoute.getEndHubId())
                .expectedDistance(deliveryRoute.getExpectedDistance())
                .expectedTime(deliveryRoute.getExpectedTime())
                .realDistance(deliveryRoute.getRealDistance())
                .realTime(deliveryRoute.getRealTime())
                .type(deliveryRoute.getType())

                .createdAt(deliveryRoute.getCreatedAt())
                .createdBy(deliveryRoute.getCreatedBy())
                .updatedAt(deliveryRoute.getUpdatedAt())
                .updatedBy(deliveryRoute.getUpdatedBy())
                .deletedAt(deliveryRoute.getDeletedAt())
                .deletedBy(deliveryRoute.getDeletedBy())
                .isDelete(deliveryRoute.isDelete())
                .build();
    }
}
