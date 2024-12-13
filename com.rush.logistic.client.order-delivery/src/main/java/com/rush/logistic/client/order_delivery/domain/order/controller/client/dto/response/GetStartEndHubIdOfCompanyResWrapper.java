package com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response;

import lombok.Builder;

public record GetStartEndHubIdOfCompanyResWrapper(
        String resultCode,
        GetStartEndHubIdOfCompanyRes result,
        String msg
) {

}
