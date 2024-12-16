package com.rush.logistic.client.order_delivery.global.util.gemini;

import com.rush.logistic.client.order_delivery.domain.order.controller.client.dto.response.HubRouteInfoRes;
import com.rush.logistic.client.order_delivery.domain.order.controller.dto.request.OrderAndDeliveryCreateReq;
import com.rush.logistic.client.order_delivery.global.util.navermap.NaverMapRes;
import lombok.Builder;

@Builder
public record GeminiReq(
        String startAddress,
        String endAddress,
        String time,
        String distance,
        String productName,
        String limitDate
) {
    public static GeminiReq toDto(OrderAndDeliveryCreateReq requestDto, HubRouteInfoRes hubRouteInfoRes, NaverMapRes naverMapRes) {
        return GeminiReq.builder()
                .startAddress(hubRouteInfoRes.startHubAddress())
                .endAddress(hubRouteInfoRes.endHubAddress())
                .limitDate(requestDto.requestDeadLine())
                .time(naverMapRes.time())
                .distance(naverMapRes.distance().toString())
                .build();
    }

    public String toStringMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("다음 배송 정보를 바탕으로 해당 상품의 배송 출발 기한 날짜와 시각을 정해줘. 답변 형식은 아래 정보들을 깔끔하게 항목으로 정리한 다음, '배송 출발 기한 날짜는 ~입니다.' 이런 거 한두문장만 넣어줘");
        sb.append(toStringInfo());
       return sb.toString();
    }

    public String toStringInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n출발 주소 : ");
        sb.append(startAddress);
        sb.append("\n도착 주소 : ");
        sb.append(endAddress);
        sb.append("\n예상 소요시간 : ");
        sb.append(time);
        sb.append("\n예상 거리 : ");
        sb.append(distance);
        sb.append("\n배송 상품명 : ");
        sb.append(productName);
        sb.append("\n배송 수령 희망 날짜 : ");
        sb.append(limitDate);
        return sb.toString();

    }
}
