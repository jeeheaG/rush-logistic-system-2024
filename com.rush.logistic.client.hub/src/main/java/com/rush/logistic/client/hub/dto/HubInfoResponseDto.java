package com.rush.logistic.client.hub.dto;

import com.rush.logistic.client.hub.model.Hub;
import com.rush.logistic.client.hub.model.HubItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubInfoResponseDto {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public static HubInfoResponseDto from(Hub hub) {
        return HubInfoResponseDto.builder()
                .name(hub.getName())
                .address(hub.getAddress())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .build();
    }

    public static HubInfoResponseDto fromRedis(HubItem hubItem) {
        return HubInfoResponseDto.builder()
                .name(hubItem.getName())
                .address(hubItem.getAddress())
                .latitude(hubItem.getLatitude())
                .longitude(hubItem.getLongitude())
                .build();
    }
}
