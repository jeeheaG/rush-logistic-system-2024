package com.rush.logistic.client.hub.dto;

import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteListResponseDto<T> {
    private int totalDistance;
    private Long totalMilliseconds;
    private String totalTimeTaken;
    private List<T> hubRouteList;

    public static <T> HubRouteListResponseDto<T> from(List<T> hubRouteList, int totalDistance, Long totalMilliseconds,
                                                 String totalTimeTaken) {
        return HubRouteListResponseDto.<T>builder()
                .hubRouteList(hubRouteList)
                .totalDistance(totalDistance)
                .totalMilliseconds(totalMilliseconds)
                .totalTimeTaken(totalTimeTaken)
                .build();
    }
}
