package com.rush.logistic.client.order_delivery.domain.delivery.service;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request.DeliveryAllReq;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryCode;
import com.rush.logistic.client.order_delivery.domain.delivery.exception.DeliveryException;
import com.rush.logistic.client.order_delivery.domain.delivery.repository.DeliveryRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.global.auth.checker.DeliveryUserRoleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryUserRoleChecker deliveryUserRoleChecker;

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

    public DeliveryAllRes getDeliveryDetail(UUID deliveryId, GetUserInfoRes getUserInfoRes) {
        Delivery delivery = getDeliveryEntityById(deliveryId);
        deliveryUserRoleChecker.checkInCharge(delivery, getUserInfoRes);

        return DeliveryAllRes.fromEntity(delivery);
    }

    @Transactional
    public DeliveryAllRes updateDelivery(UUID deliveryId, DeliveryAllReq requestDto, GetUserInfoRes getUserInfoRes) {
        Delivery delivery = getDeliveryEntityById(deliveryId);
        deliveryUserRoleChecker.checkInCharge(delivery, getUserInfoRes);

        delivery.updateAll(requestDto);
        deliveryRepository.saveAndFlush(delivery);

        Delivery savedDelivery = getDeliveryEntityById(deliveryId);
        return DeliveryAllRes.fromEntity(savedDelivery);
    }

    @Transactional
    public UUID deleteDelivery(UUID deliveryId, Long userId, GetUserInfoRes getUserInfoRes) {
        Delivery delivery = getDeliveryEntityById(deliveryId);
        deliveryUserRoleChecker.checkInCharge(delivery, getUserInfoRes);

        delivery.softDelete(userId.toString());
        return delivery.getId();
    }


    private Delivery getDeliveryEntityById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryCode.DELIVERY_NOT_EXIST));
    }

    public PagedModel<DeliveryAllRes> getDeliverySearch(GetUserInfoRes getUserInfoRes, Predicate predicate, Pageable pageRequest) {
        return deliveryRepository.findAllInPagedDto(predicate, pageRequest);

    }
}
