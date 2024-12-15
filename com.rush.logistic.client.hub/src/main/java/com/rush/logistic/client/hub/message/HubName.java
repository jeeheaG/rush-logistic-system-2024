package com.rush.logistic.client.hub.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HubName {
    SEOUL_HUB("서울특별시 센터"),
    NORTH_GYEONGGI_HUB("경기 북부 센터"),
    SOUTH_GYEONGGI_HUB("경기 남부 센터"),
    PUSAN_HUB("부산광역시 센터"),
    DAEGU_HUB("대구광역시 센터"),
    INCHEON_HUB("인천광역시 센터"),
    GWANGJU_HUB("광주광역시 센터"),
    DAEJEON_HUB("대전광역시 센터"),
    ULSAN_HUB("울산광역시 센터"),
    SEJONG_HUB("세종특별자치시 센터"),
    GANGWON_HUB("강원특별자치도 센터"),
    CHUNGBUK_HUB("충청북도 센터"),
    CHUNGNAM_HUB("충청남도 센터"),
    JEONBUK_HUB("전북특별자치도 센터"),
    JEONNAM_HUB("전라남도 센터"),
    GYEONGBUK_HUB("경상북도 센터"),
    GYEONGNAM_HUB("경상남도 센터");

    private final String message;

}
