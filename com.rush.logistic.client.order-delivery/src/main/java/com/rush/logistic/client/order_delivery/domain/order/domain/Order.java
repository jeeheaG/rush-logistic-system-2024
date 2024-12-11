package com.rush.logistic.client.order_delivery.domain.order.domain;

import com.rush.logistic.client.order_delivery.domain.delivery.domain.Delivery;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAllReq;
import com.rush.logistic.client.order_delivery.global.common.BaseAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

// TODO : validation

@Builder
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="p_order")
@SQLRestriction("is_delete = false")
//@SQLDelete(sql = "UPDATE p_order SET is_delete = true WHERE id = ?") // 직접 컬럼을 업데이트 하는 것이 조금 더 필드명 등을 객체로 관리하기에 편할 것 같고 soft delete 라는 점을 명시적으로 나타내어 줄 수 있을 것이라 판닪여 보류
public class Order extends BaseAudit {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="order_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private UUID receiveCompanyId;

    @Column(nullable = false)
    private UUID produceCompanyId;

    // TODO : temp
    private UUID deliveryId;

//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "delivery_id", nullable = false)
//    private Delivery delivery;

    // TODO : 날짜 형식
    private String requestDeadLine;

    private String requestNote;

    public void updateAll(OrderAllReq requestDto) {
        if (requestDto.productId()!=null) { this.productId = requestDto.productId(); }
        if (requestDto.quantity()!=null) { this.quantity = requestDto.quantity(); }
        if (requestDto.receiveCompanyId()!=null) { this.receiveCompanyId = requestDto.receiveCompanyId(); }
        if (requestDto.produceCompanyId()!=null) { this.produceCompanyId = requestDto.produceCompanyId(); }
        if (requestDto.deliveryId()!=null) { this.deliveryId = requestDto.deliveryId(); }
        if (requestDto.requestDeadLine()!=null) { this.requestDeadLine = requestDto.requestDeadLine(); }
        if (requestDto.requestNote()!=null) { this.requestNote = requestDto.requestNote(); }
    }

}
