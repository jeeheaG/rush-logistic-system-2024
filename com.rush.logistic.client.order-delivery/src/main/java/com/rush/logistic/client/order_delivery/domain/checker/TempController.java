package com.rush.logistic.client.order_delivery.domain.checker;

import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import com.rush.logistic.client.order_delivery.global.util.gemini.GeminiClient;
import com.rush.logistic.client.order_delivery.global.util.gemini.GeminiReq;
import com.rush.logistic.client.order_delivery.global.util.navermap.NaverMapRes;
import com.rush.logistic.client.order_delivery.global.util.navermap.NaverMapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/temp")
public class TempController {

    private final NaverMapUtil naverMapUtil;
    private final GeminiClient geminiClient;

    /**
     * navermap api 테스트용 임시 컨트롤러
     * @param startAdd
     * @param endAdd
     * @return
     */
    @GetMapping("/navermap")
    public ResponseEntity<Object> navermapTester(@RequestParam String startAdd, @RequestParam String endAdd){
        NaverMapRes response = naverMapUtil.getDistanceAndTimeByAddress(startAdd, endAdd);
        return ResponseEntity.ok().body(BaseResponse.toResponse(BasicCode.TEST_OK, response));
    }


    /**
     * gemini api 테스트용 임시 컨트롤러
     * @return
     */
    @GetMapping("/gemini")
    public ResponseEntity<Object> geminiTester(){
        GeminiReq geminiReq = GeminiReq.builder()
                .startAddress("서울시 강북구 한천로 170")
                .endAddress("경기도 용인시 수지구 광교마을로 11")
                .time("0D 8H 30M")
                .distance("12322")
                .limitDate("2024-12-25")
                .productName("크리스마스 양말")
                .build();
        String message = geminiClient.getExpectedStartTime(geminiReq);
        return ResponseEntity.ok().body(BaseResponse.toResponse(BasicCode.TEST_OK, message));
    }


}
