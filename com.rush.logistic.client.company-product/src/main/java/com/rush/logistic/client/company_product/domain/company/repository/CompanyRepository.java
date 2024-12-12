package com.rush.logistic.client.company_product.domain.company.repository;

import com.rush.logistic.client.company_product.domain.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByName(String name);

    Page<Company> findByNameContainingAndHubId(String searchKeyword, UUID hubId, Pageable pageable);

    Page<Company> findByHubId(UUID hubId, Pageable pageable);

    Page<Company> findByNameContaining(String searchKeyword, Pageable pageable);
}
