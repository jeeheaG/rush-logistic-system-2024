package com.rush.logistic.client.order_delivery.domain.deliveryman.repository;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanInChargeTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliverymanStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliverymanRepository extends JpaRepository<Deliveryman, UUID>, DeliverymanRepositoryCustom, QuerydslPredicateExecutor<Deliveryman> {
    Optional<Deliveryman> findByUserId(Long userId);

    Optional<Deliveryman> findTop1ByLastHubIdAndInChargeTypeAndStatusOrderByLastDeliveryTimeAsc(
            UUID lastHubId, DeliverymanInChargeTypeEnum inChargeType, DeliverymanStatusEnum status);
    Optional<Deliveryman> findTop1ByHubInChargeIdAndInChargeTypeAndStatusOrderByLastDeliveryTimeAsc(
            UUID hubInChargeId, DeliverymanInChargeTypeEnum inChargeType, DeliverymanStatusEnum status);

    default PagedModel<DeliverymanAllRes> findAllInPagedDto(Predicate predicate, Pageable pageRequest) {
        Page<Deliveryman> pageEntity = findAll(predicate, pageRequest); // DB 조회

        Page<DeliverymanAllRes> pageDto = pageEntity.map(DeliverymanAllRes::fromEntity);
        return new PagedModel<DeliverymanAllRes>(pageDto);
    };

}
