package com.rush.logistic.client.order_delivery.domain.delivery_route.repository;

import com.querydsl.core.types.Predicate;
import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.response.DeliveryRouteAllRes;
import com.rush.logistic.client.order_delivery.domain.delivery_route.domain.DeliveryRoute;
import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.response.DeliverymanAllRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.response.OrderAllRes;
import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID>, QuerydslPredicateExecutor<DeliveryRoute> {
    List<DeliveryRoute> findAllByDelivery(Delivery delivery);

    default PagedModel<DeliveryRouteAllRes> findAllInPagedDto(Predicate predicate, Pageable pageRequest) {
        Page<DeliveryRoute> pageEntity = findAll(predicate, pageRequest); // DB 조회

        Page<DeliveryRouteAllRes> pageDto = pageEntity.map(DeliveryRouteAllRes::fromEntity);
        return new PagedModel<DeliveryRouteAllRes>(pageDto);
    }
}
