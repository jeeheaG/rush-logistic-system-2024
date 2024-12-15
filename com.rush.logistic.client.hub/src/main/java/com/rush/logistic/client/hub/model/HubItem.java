package com.rush.logistic.client.hub.model;

import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.LatLonDto;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("hubItem")
public class HubItem implements Serializable {
    @Id
    private String hubId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public static HubItem from(UUID hubId, HubInfoRequestDto responseDto, LatLonDto latLonDto) {
        return HubItem.builder()
            .hubId(String.valueOf(hubId))
            .name(responseDto.getName())
            .address(responseDto.getAddress())
            .latitude(Double.parseDouble(latLonDto.getLatitude()))
            .longitude(Double.parseDouble(latLonDto.getLongitude()))
            .build();
    }

    public static HubItem to(Hub hub) {
        return HubItem.builder()
            .hubId(String.valueOf(hub.getHubId()))
            .name(hub.getName())
            .address(hub.getAddress())
            .latitude(hub.getLatitude())
            .longitude(hub.getLongitude())
            .build();
    }
}
