package com.rush.logistic.client.order_delivery.global.util.navermap;

import org.springframework.stereotype.Component;

@Component
public class NaverMapUtil {
    public NaverMapRes getDistanceAndTimeByAddress(String startAddress, String endAddress) {
        // TODO : api call 로직 구현

        return NaverMapRes.toDto();
    }
}
