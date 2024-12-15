package com.rush.logistic.client.order_delivery.domain.deliveryman.domain;

// TODO : validation

import com.rush.logistic.client.order_delivery.domain.deliveryman.controller.dto.request.DeliverymanUpdateReq;
import com.rush.logistic.client.order_delivery.global.common.BaseAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.rush.logistic.client.order_delivery.global.common.GlobalConst.TIME_ZONE_ID;

/**
 * user 중 role 이 deliveryman 인 사용자에 대해 deliveryman 에 대한 속성을 저장하는 엔티티
 */
@Builder
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="p_deliveryman")
@SQLRestriction("is_delete = false")
public class Deliveryman extends BaseAudit {

//    // 혹시 몰라 만들어두는 고유값 필드 (로직에서 되도록 사용은 안하고 구현 예정)
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name="deliveryman_id", updatable = false, nullable = false)
//    private UUID id;

    @Id
    @Column(updatable = false, nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliverymanInChargeTypeEnum inChargeType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliverymanStatusEnum status;

    private Integer sequence;

    @Column(nullable = false)
    private UUID hubInChargeId;

    private UUID lastHubId;

    private ZonedDateTime lastDeliveryTime;

    public void updateAll(DeliverymanUpdateReq requestDto) {
        if (requestDto.status()!=null) { this.status = requestDto.status(); }
        if (requestDto.sequence()!=null) { this.sequence = requestDto.sequence(); }
        if (requestDto.hubInChargeId()!=null) { this.hubInChargeId = requestDto.hubInChargeId(); }
        if (requestDto.lastHubId()!=null) { this.lastHubId = requestDto.lastHubId(); }
        if (requestDto.lastDeliveryTime()!=null) { this.lastDeliveryTime = requestDto.lastDeliveryTime(); }
    }

    public void updateAssigned(UUID endHubId, Integer sequence) {
        this.status = DeliverymanStatusEnum.ASSIGNED;
        this.lastHubId = endHubId;
        this.sequence = sequence;
        this.lastDeliveryTime = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
    }
}
