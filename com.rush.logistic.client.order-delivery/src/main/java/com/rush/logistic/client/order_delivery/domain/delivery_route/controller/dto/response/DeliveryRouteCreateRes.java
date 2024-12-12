package com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response;

import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record DeliveryRouteCreateRes(
        UUID id,
        DeliverymanAllRes deliveryman,
        Integer sequence,
        DeliveryRouteStatusEnum status,
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


    public static DeliveryRouteCreateRes fromEntity(DeliveryRoute deliveryRoute) {
        DeliverymanAllRes deliverymanAllRes = DeliverymanAllRes.fromEntity(deliveryRoute.getDeliveryman());

        return DeliveryRouteCreateRes.builder()
                .id(deliveryRoute.getId())
                .deliveryman(deliverymanAllRes)
                .sequence(deliveryRoute.getSequence())
                .status(deliveryRoute.getStatus())
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

    public static List<DeliveryRouteCreateRes> fromEntities(List<DeliveryRoute> deliveryRoutes) {
        return deliveryRoutes.stream().map(DeliveryRouteCreateRes::fromEntity).toList();
    }
}
