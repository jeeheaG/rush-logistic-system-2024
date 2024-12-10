package com.rush.logistic.client.company_product.domain.service;

import com.rush.logistic.client.company_product.domain.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.dto.request.CompanyCreateRequest;
import com.rush.logistic.client.company_product.domain.dto.request.CompanyUpdateRequest;
import com.rush.logistic.client.company_product.domain.dto.response.CompanySearchResponse;
import com.rush.logistic.client.company_product.domain.entity.BaseEntity;
import com.rush.logistic.client.company_product.domain.entity.Company;
import com.rush.logistic.client.company_product.domain.repository.CompanyRepository;
import com.rush.logistic.client.company_product.global.exception.ApplicationException;
import com.rush.logistic.client.company_product.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    //업체 추가
    @Transactional
    public void createCompany(CompanyCreateRequest requestDto) {
        // TODO: MASTER, HUB-MANAGER 확인 로직 추가
        CompanyDto dto = CompanyCreateRequest.toDto(requestDto);

        Optional<Company> company = companyRepository.findByName(dto.name());

        if(company.isEmpty()){
            Company companyEntity = companyRepository.save(dto.toEntity(dto));
        }else{
            throw new ApplicationException(ErrorCode.DUPLICATED_COMPANYNAME);
        }
    }

    //업체 전체 조회
    @Transactional
    public Page<CompanyDto> getAllCompany(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);
        return companies.map(CompanyDto::from);
    }

    //업체 단건 조회
    @Transactional
    public CompanySearchResponse getCompany(UUID Id) {
        Company company = companyRepository.findById(Id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REQUEST));
        return CompanySearchResponse.from(company);
    }

    //업체 수정
    @Transactional
    public CompanyDto updateCompany(UUID Id, CompanyUpdateRequest request) {
        Company company = companyRepository.findById(Id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REQUEST));

        company.setHubId(request.hubId());
        company.setName(request.name());
        company.setAddress(request.address());
        company.setType(request.type());

        Company updatedCompany = companyRepository.save(company);

        return CompanyDto.from(updatedCompany);
    }
}
