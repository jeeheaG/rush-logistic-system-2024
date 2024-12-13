package com.rush.logistic.client.company_product.domain.company.repository;

import com.rush.logistic.client.company_product.domain.company.entity.Company;
import com.rush.logistic.client.company_product.global.type.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompanyRepositoryCustom {
    Page<Company> searchCompany(String name, UUID hubId, CompanyType companyType, Pageable pageable);
}
