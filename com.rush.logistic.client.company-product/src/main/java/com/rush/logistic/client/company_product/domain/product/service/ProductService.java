package com.rush.logistic.client.company_product.domain.product.service;

import com.rush.logistic.client.company_product.domain.company.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.company.entity.Company;
import com.rush.logistic.client.company_product.domain.company.repository.CompanyRepository;
import com.rush.logistic.client.company_product.domain.product.dto.ProductDto;
import com.rush.logistic.client.company_product.domain.product.dto.request.ProductCreateRequest;
import com.rush.logistic.client.company_product.domain.product.dto.request.ProductUpdateRequest;
import com.rush.logistic.client.company_product.domain.product.dto.response.ProductSearchResponse;
import com.rush.logistic.client.company_product.domain.product.entity.Product;
import com.rush.logistic.client.company_product.domain.product.repository.ProductRepository;
import com.rush.logistic.client.company_product.global.client.ApiResponse;
import com.rush.logistic.client.company_product.global.client.UserClient;
import com.rush.logistic.client.company_product.global.client.UserResponseDto;
import com.rush.logistic.client.company_product.global.client.UserRoleEnum;
import com.rush.logistic.client.company_product.global.exception.ApplicationException;
import com.rush.logistic.client.company_product.global.exception.ErrorCode;
import com.rush.logistic.client.company_product.global.exception.Response;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.ForbiddenException;
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

    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final UserClient userClient;

    private final EntityManager entityManager;

    @Transactional
    public ProductDto createProduct(ProductCreateRequest request, String role, String authenticatedUserId) {
        // 1. 권한 검증
        validateUserPermissionForCreate(request, role, authenticatedUserId);

        // 2. 상품 생성
        return createProductEntity(request);
    }

    // 권한 검증
    private void validateUserPermissionForCreate(ProductCreateRequest request, String role, String authenticatedUserId) {
        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        UserResponseDto userData = response.getData();

        if (UserRoleEnum.MASTER.getRole().equals(role)) {
            // 마스터 관리자: 모든 상품을 추가할 수 있음
            return;
        }

        if (UserRoleEnum.HUB.getRole().equals(role)) {
            // 허브 관리자: 담당 허브에 대한 리소스만 생성 가능
            UUID hubId = UUID.fromString(userData.getHubId());
            if (!hubId.equals(request.hubId())) {
                throw new ApplicationException(ErrorCode.INVALID_ROLE);
            }
        }
        else if (UserRoleEnum.COMPANY.getRole().equals(role)) {
            // 업체 담당자: 본인 업체에 대한 리소스만 생성 가능
            UUID companyId = UUID.fromString(userData.getCompanyId());
            if (!companyId.equals(request.companyId())) {
                throw new ApplicationException(ErrorCode.INVALID_ROLE);
            }
        }else {
            throw new ApplicationException(ErrorCode.INVALID_ROLE);
        }
    }


    // 상품 생성
    private ProductDto createProductEntity(ProductCreateRequest request) {
        ProductDto dto = ProductCreateRequest.toDto(request);

        // 상품 이름 중복 체크
        Optional<Product> existingProduct = productRepository.findByName(dto.name());
        if (existingProduct.isPresent()) {
            throw new ApplicationException(ErrorCode.DUPLICATED_PRODUCTNAME);
        }

        // 상품 저장
        Product productEntity = productRepository.save(dto.toEntity(dto));
        System.out.println("Save Product Id: " + productEntity.getId());
        return ProductDto.from(productEntity);
    }


    //상품 전체 조회
    public Page<ProductDto> getAllProduct(
            String name,
            UUID companyId,
            UUID hubId,
            Pageable pageable,
            String sortType,
            String role,
            String authenticatedUserId
    ) {
        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);

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
    public ProductSearchResponse getProduct(UUID id, String role, String authenticatedUserId) {
        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getIsDelete()) {
            throw new ApplicationException(ErrorCode.PRODUCT_DELETED); // 삭제된 업체에 대한 예외
        }

        return ProductSearchResponse.from(product);
    }

    //상품 수정

    @Transactional
    public ProductDto updateProduct(UUID id, ProductUpdateRequest request, String role, String authenticatedUserId) {
        // 1. 권한 검증
        validateUserPermissionForUpdate(id, request, role, authenticatedUserId);

        // 2. 업체 수정
        return updateProductEntity(id, request);
    }

    private void validateUserPermissionForUpdate(UUID id, ProductUpdateRequest request, String role, String authenticatedUserId) {
        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        UserResponseDto userData = response.getData();

        if (UserRoleEnum.MASTER.getRole().equals(role)) {
            // 관리자는 모든 업체 수정 가능
            return;
        }

        if (UserRoleEnum.HUB.getRole().equals(role)) {
            // 허브 매니저 권한 체크
            UUID hubId = UUID.fromString(userData.getHubId());
            if (!hubId.equals(request.hubId())) {
                throw new ApplicationException(ErrorCode.INVALID_ROLE);
            }
        } else if (UserRoleEnum.COMPANY.getRole().equals(role)) {
            // 회사 관리자 권한 체크 (중복된 조건을 수정)
            UUID companyId = UUID.fromString(userData.getCompanyId());
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
            if (!companyId.equals(product.getCompanyId())) {
                throw new ApplicationException(ErrorCode.INVALID_ROLE);
            }
        } else {
            // 일반 사용자는 업체 수정 불가
            throw new ApplicationException(ErrorCode.INVALID_ROLE);
        }
    }

    private ProductDto updateProductEntity(UUID id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getIsDelete()) {
            throw new ApplicationException(ErrorCode.PRODUCT_DELETED); // 삭제된 업체에 대한 예외
        }

        // 상품 정보 수정
        product.setCompanyId(request.companyId());
        product.setHubId(request.hubId());
        product.setName(request.name());
        product.setQuantity(request.quantity());

        // DB 업데이트
        entityManager.flush();
        entityManager.clear();

        // 수정된 상품 반환
        Product productForReturn = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException((ErrorCode.PRODUCT_NOT_FOUND)));

        return ProductDto.from(productForReturn);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(UUID id, String role, String authenticatedUserId) {
        // 1. 권한 검증
        validateUserPermissionForDelete(id, role, authenticatedUserId);

        // 2. 업체 수정
        deleteProductEntity(id);
    }

    private void validateUserPermissionForDelete(UUID id, String role, String authenticatedUserId) {
        ApiResponse<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        UserResponseDto userData = response.getData();

        if (UserRoleEnum.MASTER.getRole().equals(role)) {
            // 관리자는 모든 업체 삭제 가능
            return;
        }
        if (UserRoleEnum.HUB.getRole().equals(role)) {
            // 허브 매니저 권한 체크
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REQUEST));
            UUID hubId = UUID.fromString(userData.getHubId());
            if (!hubId.equals(company.getHubId())) {
                throw new ApplicationException(ErrorCode.INVALID_ROLE);
            }
        } else {
            // 일반 사용자는 업체 삭제 불가
            throw new ApplicationException(ErrorCode.INVALID_ROLE);
        }
    }

    private void deleteProductEntity(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException((ErrorCode.PRODUCT_NOT_FOUND)));

        if (product.getIsDelete()) {
            throw new ApplicationException(ErrorCode.INVALID_REQUEST);
        }

        product.setIsDelete(true);
        product.setDeletedAt(LocalDateTime.now());
    }

}
