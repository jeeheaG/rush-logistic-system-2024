package com.rush.logistic.client.company_product.domain.service;

import com.rush.logistic.client.company_product.domain.dto.ProductDto;
import com.rush.logistic.client.company_product.domain.dto.request.ProductCreateRequest;
import com.rush.logistic.client.company_product.domain.dto.request.ProductUpdateRequest;
import com.rush.logistic.client.company_product.domain.dto.response.ProductSearchResponse;
import com.rush.logistic.client.company_product.domain.entity.Product;
import com.rush.logistic.client.company_product.domain.repository.ProductRepository;
import com.rush.logistic.client.company_product.global.exception.ApplicationException;
import com.rush.logistic.client.company_product.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final EntityManager entityManager;

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

    //상품 전체 조회
    public Page<ProductDto> getAllProduct(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDto::from);
    }

    //상품 단건 조회
    public ProductSearchResponse getProduct(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductSearchResponse.from(product);
    }

    //상품 수정

    @Transactional
    public ProductDto updateProduct(UUID id, ProductUpdateRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException((ErrorCode.PRODUCT_NOT_FOUND)));

        product.setCompanyId(request.companyId());
        product.setHubId(request.hubId());
        product.setName(request.name());
        product.setQuantity(request.quantity());

        entityManager.flush();

        entityManager.clear();

        Product productForReturn = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException((ErrorCode.PRODUCT_NOT_FOUND)));

        return ProductDto.from(productForReturn);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException((ErrorCode.PRODUCT_NOT_FOUND)));

        if ( product.getIsDelete()){
            throw new ApplicationException(ErrorCode.INVALID_REQUEST);
        }

        product.setIsDelete(true);
        product.setDeletedAt(LocalDateTime.now());
    }
}
