package com.rush.logistic.client.company_product.domain.company.dto.response;

import java.util.UUID;

public  record CompanyHubMappingResponse(
        UUID departureHubId,
        UUID arrivalHubId
) {

}
