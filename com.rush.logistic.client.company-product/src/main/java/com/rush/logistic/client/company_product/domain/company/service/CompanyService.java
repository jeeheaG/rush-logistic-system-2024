package com.rush.logistic.client.company_product.domain.company.service;

import com.rush.logistic.client.company_product.domain.company.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.company.dto.request.CompanyCreateRequest;
import com.rush.logistic.client.company_product.domain.company.dto.request.CompanyHubMappingRequest;
import com.rush.logistic.client.company_product.domain.company.dto.request.CompanyUpdateRequest;
import com.rush.logistic.client.company_product.domain.company.dto.response.CompanyHubMappingResponse;
import com.rush.logistic.client.company_product.domain.company.dto.response.CompanySearchResponse;
import com.rush.logistic.client.company_product.domain.company.entity.Company;
import com.rush.logistic.client.company_product.domain.company.repository.CompanyRepository;
import com.rush.logistic.client.company_product.global.client.UserClient;
import com.rush.logistic.client.company_product.global.client.UserRoleEnum;
import com.rush.logistic.client.company_product.global.client.UserResponseDto;
import com.rush.logistic.client.company_product.global.exception.ApplicationException;
import com.rush.logistic.client.company_product.global.exception.ErrorCode;
import com.rush.logistic.client.company_product.global.exception.Response;
import com.rush.logistic.client.company_product.global.type.CompanyType;
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
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserClient userClient;

    private final EntityManager entityManager;

    //업체 추가
    @Transactional
    public CompanyDto createCompany(CompanyCreateRequest request, String role, String authenticatedUserId) {
        Response<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        UserResponseDto userResponseDto = response.getResult();

        // 관리자(MASTER) 권한 체크
        if (UserRoleEnum.MASTER.getRole().equals(role)) {
            // 관리자는 모든 업체 생성 가능
        } else if (UserRoleEnum.HUB.getRole().equals(role)) {
            // 허브 매니저 권한 체크
            if (userResponseDto.getHubId().equals(request.hubId())) {
            } else {
                throw new ForbiddenException("해당 업체에 대한 관리자 권한이 없습니다.");
            }
        } else {
            // 일반 사용자는 업체 생성 불가
            throw new ForbiddenException("업체 생성 권한이 없습니다.");
        }

        CompanyDto dto = CompanyCreateRequest.toDto(request);

        Optional<Company> company = companyRepository.findByName(dto.name());

        if(company.isEmpty()){
            Company companyEntity = companyRepository.save(dto.toEntity(dto));
            System.out.println("Saved Company ID: " + companyEntity.getId());
            return CompanyDto.from(companyEntity);
        }else{
            throw new ApplicationException(ErrorCode.DUPLICATED_COMPANYNAME);
        }
    }

    //업체 전체 조회
    @Transactional
    public Page<CompanyDto> getAllCompany(
            String name,
            UUID hubId,
            CompanyType companyType,
            Pageable pageable,
            String sortType,
            String role,
            String authenticatedUserId
    ) {
        Response<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);

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

        // 검색 조건 처리
        Page<Company> companies = companyRepository.searchCompany(
                name,           // 회사명
                hubId,          // 허브 ID
                companyType,    // 회사 타입
                pageable
        );

        // DTO 변환
        return companies.map(CompanyDto::from);
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

    //업체 단건 조회
    @Transactional
    public CompanySearchResponse getCompany(UUID id, String role, String authenticatedUserId) {
        Response<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMPANY_NOT_FOUND));

        if (company.getIsDelete()) {
            throw new ApplicationException(ErrorCode.COMPANY_DELETED); // 삭제된 업체에 대한 예외
        }

            return CompanySearchResponse.from(company);
    }

    //업체 수정
    @Transactional
    public CompanyDto updateCompany(UUID id, CompanyUpdateRequest request, String role, String authenticatedUserId) {

        Response<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        UserResponseDto userResponseDto = response.getResult();

        // 관리자(MASTER) 권한 체크
        if (UserRoleEnum.MASTER.getRole().equals(role)) {
            // 관리자는 모든 업체 생성 가능
        } else if (UserRoleEnum.HUB.getRole().equals(role)) {
            // 허브 매니저 권한 체크
            if (userResponseDto.getHubId().equals(request.hubId())) {
            } else {
                throw new ForbiddenException("해당 업체에 대한 관리자 권한이 없습니다.");
            }
        } else if (UserRoleEnum.HUB.getRole().equals(role)) {
            if (userResponseDto.getCompanyId().equals(companyRepository.findById(id))) {
            } else {
                throw new ForbiddenException("해당 업체에 대한 관리자 권한이 없습니다.");
            }
        } else {
            // 일반 사용자는 업체 생성 불가
            throw new ForbiddenException("업체 수정 권한이 없습니다.");
        }
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMPANY_NOT_FOUND));

        if (company.getIsDelete()) {
            throw new ApplicationException(ErrorCode.COMPANY_DELETED); // 삭제된 업체에 대한 예외
        }

        company.setHubId(request.hubId());
        company.setName(request.name());
        company.setAddress(request.address());
        company.setType(request.type());

        entityManager.flush();

        entityManager.clear();

        Company companyForReturn = companyRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMPANY_NOT_FOUND));

        return CompanyDto.from(companyForReturn);
    }

    //업체 삭제
    @Transactional
    public void deleteCompany(UUID id, String role, String authenticatedUserId) {

        Response<UserResponseDto> response = userClient.getUserById(authenticatedUserId, role, authenticatedUserId);
        UserResponseDto userResponseDto = response.getResult();

        // 관리자(MASTER) 권한 체크
        if (UserRoleEnum.MASTER.getRole().equals(role)) {
            // 관리자는 모든 업체 생성 가능
        } else if (UserRoleEnum.HUB.getRole().equals(role)) {
            // 허브 매니저 권한 체크
            if (userResponseDto.getHubId().equals(companyRepository.findById(id))) {
            } else {
                throw new ForbiddenException("해당 업체에 대한 관리자 권한이 없습니다.");
            }
        } else {
            // 일반 사용자는 업체 생성 불가
            throw new ForbiddenException("업체 삭제 권한이 없습니다.");
        }
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REQUEST));
        if (company.getIsDelete()){
            throw new ApplicationException(ErrorCode.COMPANY_DELETED);
        }
        company.setIsDelete(true);
        company.setDeletedAt(LocalDateTime.now());
    }

    //업체 - 허브 맵핑
    @Transactional(readOnly = true)
    public CompanyHubMappingResponse getCompanyHubMapping(CompanyHubMappingRequest request) {
        // 출발 업체 허브 조회
        Company departureCompany = companyRepository.findById(request.departureCompanyId())
                .orElseThrow(() -> new RuntimeException("출발 업체를 찾을 수 없습니다: " + request.departureCompanyId()));

        // 도착 업체 허브 조회
        Company arrivalCompany = companyRepository.findById(request.arrivalCompanyId())
                .orElseThrow(() -> new RuntimeException("도착 업체를 찾을 수 없습니다: " + request.arrivalCompanyId()));

        // 응답 생성 및 반환
        return new CompanyHubMappingResponse(
                departureCompany.getHubId(),
                arrivalCompany.getHubId()
        );
    }
}
