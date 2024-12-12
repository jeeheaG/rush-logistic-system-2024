package com.rush.logistic.client.hub.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteIdResponseDto {
    private UUID hubRouteId;

    public static HubRouteIdResponseDto from(UUID hubRouteId) {
        return HubRouteIdResponseDto.builder()
            .hubRouteId(hubRouteId)
            .build();
    }
}
