package com.rush.logistic.client.order_delivery.domain.delivery_route.domain;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.delivery_route.controller.dto.request.DeliveryRouteUpdateReq;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryRouteStatusEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.DeliveryTypeEnum;
import com.rush.logistic.client.order_delivery.domain.deliveryman.domain.Deliveryman;
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
@Table(name="p_delivery_route")
@SQLRestriction("is_delete = false")
public class DeliveryRoute extends BaseAudit {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="delivery_route_id", updatable = false, nullable = false)
    private UUID id;

//    @OneToOne(fetch=FetchType.LAZY, optional = false)
//    @JoinColumn(name="user_id", nullable = false)
//    private Deliveryman deliveryman;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryRouteStatusEnum status;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name="delivery_id")
//    private Delivery delivery;

    @Column(nullable = false)
    private UUID startHubId;

    private UUID endHubId;

    @Column(nullable = false)
    private Integer expectedDistance;

    @Column(nullable = false)
    private Integer expectedTime;

    private Integer realDistance;

    private Integer realTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryTypeEnum type;

    public void updateAll(DeliveryRouteUpdateReq requestDto) {
//        if (requestDto.deliveryman()!=null) { this.deliveryman = requestDto.deliveryman(); }
        if (requestDto.sequence()!=null) { this.sequence = requestDto.sequence(); }
        if (requestDto.status()!=null) { this.status = requestDto.status(); }
//        if (requestDto.delivery()!=null) { this.delivery = requestDto.delivery(); }
        if (requestDto.startHubId()!=null) { this.startHubId = requestDto.startHubId(); }
        if (requestDto.endHubId()!=null) { this.endHubId = requestDto.endHubId(); }
        if (requestDto.expectedDistance()!=null) { this.expectedDistance = requestDto.expectedDistance(); }
        if (requestDto.expectedTime()!=null) { this.expectedTime = requestDto.expectedTime(); }
        if (requestDto.realDistance()!=null) { this.realDistance = requestDto.realDistance(); }
        if (requestDto.realTime()!=null) { this.realTime = requestDto.realTime(); }
        if (requestDto.type()!=null) { this.type = requestDto.type(); }
    }
}
