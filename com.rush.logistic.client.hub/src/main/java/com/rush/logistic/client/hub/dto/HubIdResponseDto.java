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
public class HubIdResponseDto {
    private UUID hubId;

    public static HubIdResponseDto from(UUID hubId) {
        return HubIdResponseDto.builder()
            .hubId(hubId)
            .build();
    }
}
