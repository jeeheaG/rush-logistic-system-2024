package com.rush.logistic.client.order_delivery.domain.delivery.service;

import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request.DeliveryAllReq;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final EntityManager entityManager;

//    /**
//     * OrderService 쪽에서 호출됨
//     * @param requestModel
//     * @return
//     */
//    @Transactional
//    public Delivery createDelivery(DeliveryAllReq requestModel) {
//        Delivery delivery = requestModel.toEntity();
//        return deliveryRepository.save(delivery);
//    }

    public DeliveryAllRes getDeliveryDetail(UUID deliveryId) {
        Delivery delivery = getDeliveryEntityById(deliveryId);
        return DeliveryAllRes.fromEntity(delivery);
    }

    @Transactional
    public DeliveryAllRes updateDelivery(UUID deliveryId, DeliveryAllReq requestDto) {
        Delivery delivery = getDeliveryEntityById(deliveryId);
        delivery.updateAll(requestDto);
        entityManager.flush();

        Delivery savedDelivery = getDeliveryEntityById(deliveryId);
        return DeliveryAllRes.fromEntity(savedDelivery);
    }

    @Transactional
    public UUID deleteDelivery(UUID deliveryId, UUID userId) {
        Delivery delivery = getDeliveryEntityById(deliveryId);
        delivery.softDelete(userId.toString());
        return delivery.getId();
    }


    private Delivery getDeliveryEntityById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryCode.DELIVERY_NOT_EXIST));
    }
}
