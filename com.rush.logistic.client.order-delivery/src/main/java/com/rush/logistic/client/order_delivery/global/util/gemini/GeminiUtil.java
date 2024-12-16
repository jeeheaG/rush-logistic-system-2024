package com.rush.logistic.client.order_delivery.global.util.gemini;

import org.springframework.stereotype.Component;

@Component
public class GeminiUtil {
    public GeminiRes getExpectedStartDateMessage() {
        // api call 로직 구현


        return GeminiRes.toDto();
    }
}
