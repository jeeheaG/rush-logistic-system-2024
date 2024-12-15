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
public class HubPointRequestDto {
    private UUID startHubId;
    private UUID endHubId;

    public static HubPointRequestDto from(UUID startHubId, UUID endHubId) {
        return HubPointRequestDto.builder()
                .startHubId(startHubId)
                .endHubId(endHubId)
                .build();
    }
}
