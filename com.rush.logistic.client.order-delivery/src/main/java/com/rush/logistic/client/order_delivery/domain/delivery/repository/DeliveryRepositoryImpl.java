package com.rush.logistic.client.order_delivery.domain.delivery.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeliveryRepositoryImpl {
    private final JPAQueryFactory queryFactory;
}
