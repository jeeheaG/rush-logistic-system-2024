package com.rush.logistic.client.order_delivery.domain.deliveryman.service;

import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanCreateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanUpdateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanCode;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanException;
import com.rush.logistic.client.order_delivery.domain.deliveryman.repository.DeliverymanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliverymanService {
    private final DeliverymanRepository deliverymanRepository;

    @Transactional
    public DeliverymanAllRes createDeliveryman(DeliverymanCreateReq requestDto) {
        Deliveryman deliveryman = requestDto.toEntity();
        Deliveryman deliverymanSaved = deliverymanRepository.save(deliveryman);
        return DeliverymanAllRes.fromEntity(deliverymanSaved);
    }

    public DeliverymanAllRes getDeliverymanDetail(Long userId) {
        Deliveryman deliveryman = getDeliverymanEntityById(userId);
        return DeliverymanAllRes.fromEntity(deliveryman);
    }

    @Transactional
    public DeliverymanAllRes updateDeliveryman(Long userId, DeliverymanUpdateReq requestDto) {
        Deliveryman deliveryman = getDeliverymanEntityById(userId);
        deliveryman.updateAll(requestDto);
        return DeliverymanAllRes.fromEntity(deliveryman);
    }

    @Transactional
    public Long deleteDeliveryman(Long userId, Long deleteUserId) {
        Deliveryman deliveryman = getDeliverymanEntityById(userId);
        deliveryman.softDelete(deleteUserId.toString());
        return deliveryman.getUserId();
    }


    private Deliveryman getDeliverymanEntityById(Long userId) {
        return deliverymanRepository.findByUserId(userId)
                .orElseThrow(() -> new DeliverymanException(DeliverymanCode.DELIVERYMAN_NOT_EXIST));
    }

}
