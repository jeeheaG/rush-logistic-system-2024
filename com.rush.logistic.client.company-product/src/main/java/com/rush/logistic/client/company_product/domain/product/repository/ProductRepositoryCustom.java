package com.rush.logistic.client.company_product.domain.product.repository;

import com.rush.logistic.client.company_product.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepositoryCustom {
    Page<Product> searchProduct(String name, UUID companyId, UUID hubId, Pageable pageable);
}
