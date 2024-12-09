package com.rush.logistic.client.company_product.domain.service;

import com.rush.logistic.client.company_product.domain.dto.CompanyDto;
import com.rush.logistic.client.company_product.domain.dto.request.CompanyCreateRequest;
import com.rush.logistic.client.company_product.domain.entity.Company;
import com.rush.logistic.client.company_product.domain.repository.CompanyRepository;
import com.rush.logistic.client.company_product.global.exception.ApplicationException;
import com.rush.logistic.client.company_product.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    //업체 추가
    @Transactional
    public void createCompany(CompanyCreateRequest requestDto) {
        CompanyDto dto = CompanyCreateRequest.toDto(requestDto);

        Optional<Company> company = companyRepository.findByName(dto.name());

        if(company.isEmpty()){
            Company companyEntity = companyRepository.save(dto.toEntity(dto));
        }else{
            throw new ApplicationException(ErrorCode.DUPLICATED_COMPANYNAME);
        }
    }
}
