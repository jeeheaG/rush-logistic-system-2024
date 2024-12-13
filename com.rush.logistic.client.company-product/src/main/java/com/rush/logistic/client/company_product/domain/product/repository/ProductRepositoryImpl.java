package com.rush.logistic.client.company_product.domain.product.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import com.rush.logistic.client.company_product.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


import static com.rush.logistic.client.company_product.domain.product.entity.QProduct.product;

@Repository
public class ProductRepositoryImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> searchProduct(String name, UUID companyId, UUID hubId, Pageable pageable) {

        JPQLQuery<Product> query = from(product)
                .where(
                        containName(name),
                        contatinCompanyId(companyId),
                        containHubId(hubId),
                        product.isDelete.eq(false)
                );
        List<Product> products = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(products, pageable, query.fetchCount());
    }


    private Predicate containName(String name) {
        return StringUtils.hasText(name) ? product.name.containsIgnoreCase(name) : null;
    }

    private Predicate contatinCompanyId(UUID companyId) {
        return companyId != null ? product.companyId.eq(companyId) : null;
    }

    private Predicate containHubId(UUID hubId) {
        return hubId != null ? product.hubId.eq(hubId) : null;
    }

}
