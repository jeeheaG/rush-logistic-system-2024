package com.rush.logistic.client.company_product.domain.company.dto.response;

import com.rush.logistic.client.company_product.domain.company.entity.Company;
import com.rush.logistic.client.company_product.global.type.CompanyType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CompanySearchResponse(
        UUID hubId,
        String name,
        String address,
        CompanyType type
) {
    // entity -> searchDto  새로 생성하는 경우
    public static CompanySearchResponse from(Company company) {
        return CompanySearchResponse.builder()
                .hubId(company.getHubId())
                .name(company.getName())
                .address(company.getAddress())
                .type(company.getType())
                .build();
    }
}
