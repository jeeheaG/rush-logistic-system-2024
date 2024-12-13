package com.rush.logistic.client.hub.dto;

import com.rush.logistic.client.hub.model.HubRoute;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteInfoResponseDto {
    private UUID startHubId;
    private String startHubName;
    private String startHubAddress;
    private UUID endHubId;
    private String endHubName;
    private String endHubAddress;
    private String timeTaken;
    private int distance;
    private String milliseconds;

    public static HubRouteInfoResponseDto from(HubRoute hubRoute,
                                               String startHubName, String startHubAddress,
                                               String endHubName, String endHubAddress) {
        return HubRouteInfoResponseDto.builder()
                .startHubId(hubRoute.getStartHubId())
                .startHubName(startHubName)
                .startHubAddress(startHubAddress)
                .endHubId(hubRoute.getEndHubId())
                .endHubName(endHubName)
                .endHubAddress(endHubAddress)
                .timeTaken(hubRoute.getTimeTaken())
                .distance(hubRoute.getDistance())
                .milliseconds(hubRoute.getMilliseconds())
                .build();
    }
}
