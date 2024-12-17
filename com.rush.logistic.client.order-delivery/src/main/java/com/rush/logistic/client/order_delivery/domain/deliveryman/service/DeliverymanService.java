package com.rush.logistic.client.order_delivery.domain.deliveryman.service;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanCreateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanUpdateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanCode;
import com.rush.logistic.client.order_delivery.domain.deliveryman.exception.DeliverymanException;
import com.rush.logistic.client.order_delivery.domain.deliveryman.repository.DeliverymanRepository;
import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.GetUserInfoRes;
import com.rush.logistic.client.order_delivery.global.auth.checker.DeliverymanUserRoleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliverymanService {
    private final DeliverymanRepository deliverymanRepository;
    private final DeliverymanUserRoleChecker deliverymanUserRoleChecker;

    @Transactional
    public DeliverymanAllRes createDeliveryman(DeliverymanCreateReq requestDto, GetUserInfoRes getUserInfoRes) {
        Deliveryman deliveryman = requestDto.toEntity();
        deliverymanUserRoleChecker.checkInCharge(deliveryman, getUserInfoRes);

        Deliveryman deliverymanSaved = deliverymanRepository.save(deliveryman);
        return DeliverymanAllRes.fromEntity(deliverymanSaved);
    }

    public DeliverymanAllRes getDeliverymanDetail(Long userId, GetUserInfoRes getUserInfoRes) {
        Deliveryman deliveryman = getDeliverymanEntityById(userId);
        deliverymanUserRoleChecker.checkInCharge(deliveryman, getUserInfoRes);

        return DeliverymanAllRes.fromEntity(deliveryman);
    }

    @Transactional
    public DeliverymanAllRes updateDeliveryman(Long userId, DeliverymanUpdateReq requestDto, GetUserInfoRes getUserInfoRes) {
        Deliveryman deliveryman = getDeliverymanEntityById(userId);
        deliverymanUserRoleChecker.checkInCharge(deliveryman, getUserInfoRes);

        deliveryman.updateAll(requestDto);
        return DeliverymanAllRes.fromEntity(deliveryman);
    }

    @Transactional
    public Long deleteDeliveryman(Long userId, Long deleteUserId, GetUserInfoRes getUserInfoRes) {
        Deliveryman deliveryman = getDeliverymanEntityById(userId);
        deliverymanUserRoleChecker.checkInCharge(deliveryman, getUserInfoRes);

        deliveryman.softDelete(deleteUserId.toString());
        return deliveryman.getUserId();
    }

    private Deliveryman getDeliverymanEntityById(Long userId) {
        return deliverymanRepository.findByUserId(userId)
                .orElseThrow(() -> new DeliverymanException(DeliverymanCode.DELIVERYMAN_NOT_EXIST));
    }

    public PagedModel<DeliverymanAllRes> getDeliverymanSearch(GetUserInfoRes getUserInfoRes, Predicate predicate, Pageable pageRequest) {
        return deliverymanRepository.findAllInPagedDto(predicate, pageRequest);
    }
}
