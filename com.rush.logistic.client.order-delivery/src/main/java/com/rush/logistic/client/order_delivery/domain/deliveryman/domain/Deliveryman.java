package com.rush.logistic.client.order_delivery.domain.deliveryman.domain;

// TODO : validation

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
@Table(name="p_deliveryman")
@SQLRestriction("is_delete = false")
public class Deliveryman extends BaseAudit {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="delivery_id", updatable = false, nullable = false)
    private UUID id;


}
