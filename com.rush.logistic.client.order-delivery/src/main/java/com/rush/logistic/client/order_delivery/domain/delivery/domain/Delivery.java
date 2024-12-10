package com.rush.logistic.client.order_delivery.domain.delivery.domain;

import com.rush.logistic.client.order_delivery.domain.order.domain.Order;
import com.rush.logistic.client.order_delivery.global.common.BaseAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Builder
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="p_delivery")
@SQLRestriction("is_delete = false")
public class Delivery extends BaseAudit {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="delivery_id", updatable = false, nullable = false)
    private UUID id;

    // TODO : 단방향으로 바꾼다면 여길 없애야
    @OneToOne(mappedBy = "delivery", optional = false) // soft delete - cascade 생략
    private Order order;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatusEnum status;

    @Column(nullable = false)
    private UUID startHubId;

    @Column(nullable = false)
    private UUID endHubId;

    @Column(nullable = false)
    private String address;

    private String receiverSlackId;

    @Column(nullable = false)
    private UUID receiverId;
}
