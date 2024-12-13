package com.rush.logistic.client.company_product.domain.company.repository;

import com.rush.logistic.client.company_product.domain.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID>, CompanyRepositoryCustom {

    Optional<Company> findByName(String name);

    Page<Company> findByNameContainingAndHubIdAndIsDeleteFalse(String name, UUID hubId, Pageable pageable);

    // 검색어와 isDelete가 false인 조건
    Page<Company> findByNameContainingAndIsDeleteFalse(String name, Pageable pageable);

    // HubId와 isDelete가 false인 조건
    Page<Company> findByHubIdAndIsDeleteFalse(UUID hubId, Pageable pageable);

    // isDelete가 false인 모든 업체 조회
    Page<Company> findByIsDeleteFalse(Pageable pageable);

    Optional<Company> findByIdAndIsDeleteFalse(UUID id);
}
