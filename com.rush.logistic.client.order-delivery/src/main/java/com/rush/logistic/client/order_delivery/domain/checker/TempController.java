package com.rush.logistic.client.order_delivery.domain.checker;

import com.rush.logistic.client.order_delivery.global.response.BaseResponse;
import com.rush.logistic.client.order_delivery.global.response.BasicCode;
import com.rush.logistic.client.order_delivery.global.util.gemini.GeminiRes;
import com.rush.logistic.client.order_delivery.global.util.gemini.GeminiUtil;
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
    private final GeminiUtil geminiUtil;

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
     * @param req
     * @return
     */
    @GetMapping("/gemini")
    public ResponseEntity<Object> geminiTester(@RequestParam String req){
        GeminiRes response = geminiUtil.getExpectedStartDateMessage();
        return ResponseEntity.ok().body(BaseResponse.toResponse(BasicCode.TEST_OK, response));
    }


}
