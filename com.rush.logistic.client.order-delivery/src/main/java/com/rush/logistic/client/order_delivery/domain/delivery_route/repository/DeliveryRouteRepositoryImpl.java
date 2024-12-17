package com.rush.logistic.client.order_delivery.domain.delivery_route.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepositoryCustom {
    private final JPAQueryFactory queryFactory;
}
