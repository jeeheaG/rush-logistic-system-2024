package com.rush.logistic.client.company_product.domain.company.entity;

import com.rush.logistic.client.company_product.global.entity.BaseEntity;
import com.rush.logistic.client.company_product.global.type.CompanyType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_company")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(name = "hub_id")
    private UUID hubId;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CompanyType type;

    @Setter
    @Column(name = "address")
    private String address;

}
