package com.rush.logistic.client.company_product.domain.company.dto.request;

import java.util.UUID;

public record CompanyHubMappingRequest(
    UUID departureCompanyId,
    UUID arrivalCompanyId
) {

}
