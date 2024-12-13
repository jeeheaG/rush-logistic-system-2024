package com.rush.logistic.client.company_product.domain.product.service;

import com.rush.logistic.client.company_product.domain.company.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.product.dto.ProductDto;
import com.rush.logistic.client.company_product.domain.product.dto.request.ProductCreateRequest;
import com.rush.logistic.client.company_product.domain.product.dto.request.ProductUpdateRequest;
import com.rush.logistic.client.company_product.domain.product.dto.response.ProductSearchResponse;
import com.rush.logistic.client.company_product.domain.product.entity.Product;
import com.rush.logistic.client.company_product.domain.product.repository.ProductRepository;
import com.rush.logistic.client.company_product.global.exception.ApplicationException;
import com.rush.logistic.client.company_product.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    public Page<ProductDto> getAllProduct(
            String name,
            UUID companyId,
            UUID hubId,
            Pageable pageable,
            String sortType
    ) {
        // 페이지 사이즈 제한
        int[] allowedPageSizes = {10, 30, 50};
        int pageSize = pageable.getPageSize();

        // 허용되지 않은 페이지 사이즈일 경우 기본값 10으로 설정
        if (!Arrays.stream(allowedPageSizes).anyMatch(size -> size == pageSize)) {
            pageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        }

        // 정렬 로직 추가
        Sort sort = determineSort(sortType);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        //검색 조건 처리
        Page<Product> products = productRepository.searchProduct(
                name,
                companyId,
                hubId,
                pageable
        );

        // DTO 변환
        return products.map(ProductDto::from);
    }

    // 정렬 타입에 따른 Sort 결정 메서드
    private Sort determineSort(String sortType) {
        if (StringUtils.isEmpty(sortType)) {
            // 기본 정렬: 생성일 내림차순
            return Sort.by("createdAt").descending();
        }

        switch (sortType) {
            case "createdAtAsc":
                return Sort.by("createdAt").ascending();
            case "updatedAtDesc":
                return Sort.by("updatedAt").descending();
            case "updatedAtAsc":
                return Sort.by("updatedAt").ascending();
            default:
                return Sort.by("createdAt").descending();
        }
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
