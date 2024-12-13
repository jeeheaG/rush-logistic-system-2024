package com.rush.logistic.client.company_product.domain.company.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import com.rush.logistic.client.company_product.domain.company.entity.Company;
import com.rush.logistic.client.company_product.global.type.CompanyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.rush.logistic.client.company_product.domain.company.entity.QCompany.company;

@Slf4j
@Repository
public class CompanyRepositoryImpl extends QuerydslRepositorySupport implements CompanyRepositoryCustom {

    public CompanyRepositoryImpl() {
        super(Company.class);
    }

    @Override
    public Page<Company> searchCompany(String name, UUID hubId, CompanyType companyType, Pageable pageable) {
        log.info("Repository - companyName: {}", name);

        JPQLQuery<Company> query = from(company)
                .where(
                        containName(name),
                        containHubId(hubId),
                        containCompanyType(companyType),
                        company.isDelete.eq(false)
                );

        List<Company> companies = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(companies, pageable, query.fetchCount());
    }

    private Predicate containName(String name) {
        return StringUtils.hasText(name) ? company.name.containsIgnoreCase(name) : null;
    }

    private Predicate containHubId(UUID hubId) {
        return hubId != null ? company.hubId.eq(hubId) : null;
    }

    private Predicate containCompanyType(CompanyType companyType) {
        return Optional.ofNullable(companyType)
                .map(type -> company.type.eq(type))
                .orElse(null);
    }

}