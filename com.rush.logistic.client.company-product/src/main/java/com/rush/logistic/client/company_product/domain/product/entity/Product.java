package com.rush.logistic.client.company_product.domain.product.entity;

import com.rush.logistic.client.company_product.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(name = "company_id")
    private UUID companyId;

    @Setter
    @Column(name = "hub_id")
    private UUID hubId;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
