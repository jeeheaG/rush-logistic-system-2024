package com.rush.logistic.client.company_product.domain.service;

import com.rush.logistic.client.company_product.domain.dto.ProductDto;
import com.rush.logistic.client.company_product.domain.dto.request.ProductCreateRequest;
import com.rush.logistic.client.company_product.domain.entity.Product;
import com.rush.logistic.client.company_product.domain.repository.ProductRepository;
import com.rush.logistic.client.company_product.global.exception.ApplicationException;
import com.rush.logistic.client.company_product.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //상품 추가
    @Transactional
    public ProductDto createProduct(ProductCreateRequest request){
        // TODO: MASTER, HUB-MANAGER, COMPANY-MANAGER 확인 로직 추가
        ProductDto dto = ProductCreateRequest.toDto(request);

        Optional<Product> product = productRepository.findByName(dto.name());

        if(product.isEmpty()){
            Product productEntity = productRepository.save(dto.toEntity(dto));
            System.out.println("Save Product Id: " + productEntity.getId());
            return ProductDto.from(productEntity);
        }else{
            throw new ApplicationException(ErrorCode.DUPLICATED_PRODUCTNAME);
        }
    }
}
