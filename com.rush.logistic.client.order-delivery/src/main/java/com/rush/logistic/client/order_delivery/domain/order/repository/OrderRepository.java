package com.rush.logistic.client.order_delivery.domain.order.repository;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom, QuerydslPredicateExecutor<Order> {
    Logger log = LoggerFactory.getLogger(OrderRepository.class);

    Optional<Order> findByDelivery(Delivery delivery);

    /**
     * predicate 와 pageable 을 인자로 받아 조회한 결과를 PagedModel 로 감싼 DTO 목록으로 반환해주는 메서드
     * @param predicate
     * @param pageRequest
     * @return
     */
    default PagedModel<OrderAllRes> findAllInPagedDto(Predicate predicate, Pageable pageRequest) {
        Page<Order> pageOrder = findAll(predicate, pageRequest); // DB 조회

        Page<OrderAllRes> pageDto = pageOrder.map(OrderAllRes::fromEntity);
        return new PagedModel<OrderAllRes>(pageDto);
    }
}
