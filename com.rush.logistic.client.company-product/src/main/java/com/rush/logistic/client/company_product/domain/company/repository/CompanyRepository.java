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

}
