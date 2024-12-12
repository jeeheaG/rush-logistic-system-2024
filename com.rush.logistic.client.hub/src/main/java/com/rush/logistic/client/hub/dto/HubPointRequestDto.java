package com.rush.logistic.client.hub.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubPointRequestDto {
    private UUID startHubId;
    private UUID endHubId;
}
