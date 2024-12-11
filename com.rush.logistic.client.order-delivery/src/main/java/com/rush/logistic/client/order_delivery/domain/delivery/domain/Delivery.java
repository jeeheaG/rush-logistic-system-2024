package com.rush.logistic.client.order_delivery.domain.delivery.domain;

import com.rush.logistic.client.order_delivery.domain.delivery.controller.dto.request.DeliveryAllReq;
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

//    // TODO : 단방향으로 바꾼다면 여길 없애야
//    @OneToOne(mappedBy = "delivery") // soft delete - cascade 생략 //, optional = false
//    private Order order;

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

    public void updateAll(DeliveryAllReq requestDto) {
//        if (requestDto.order()!=null) { this.order = requestDto.order(); }
        if (requestDto.address()!=null) { this.address = requestDto.address(); }
        if (requestDto.status()!=null) { this.status = DeliveryStatusEnum.valueOf(requestDto.status()); }
        if (requestDto.startHubId()!=null) { this.startHubId = requestDto.startHubId(); }
        if (requestDto.endHubId()!=null) { this.endHubId = requestDto.endHubId(); }
        if (requestDto.address()!=null) { this.address = requestDto.address(); }
        if (requestDto.receiverSlackId()!=null) { this.receiverSlackId = requestDto.receiverSlackId(); }
        if (requestDto.receiverId()!=null) { this.receiverId = requestDto.receiverId(); }
    }
}
