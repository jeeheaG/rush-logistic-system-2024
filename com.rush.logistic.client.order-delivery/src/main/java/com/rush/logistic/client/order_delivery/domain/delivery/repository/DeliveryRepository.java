package com.rush.logistic.client.order_delivery.domain.delivery.repository;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.response.DeliveryAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID>, QuerydslPredicateExecutor<Delivery> {

    default PagedModel<DeliveryAllRes> findAllInPagedDto(Predicate predicate, Pageable pageRequest) {
        Page<Delivery> pageEntity = findAll(predicate, pageRequest); // DB 조회

        Page<DeliveryAllRes> pageDto = pageEntity.map(DeliveryAllRes::fromEntity);
        return new PagedModel<DeliveryAllRes>(pageDto);
    }
}
