package com.rush.logistic.client.order_delivery.domain.order.domain;

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
@SQLDelete(sql = "UPDATE p_order SET is_delete = true WHERE id = ?")
public class Order extends BaseAudit {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="order_id", updatable = false, nullable = false)
    private UUID id;

    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private UUID receiveCompanyId;

    @Column(nullable = false)
    private UUID produceCompanyId;

    @Column(nullable = false)
    private UUID deliveryId;

    // TODO : 날짜 형식
    private String requestDeadLine;

    private String requestNote;

    public void updateAll() {

    }

}
