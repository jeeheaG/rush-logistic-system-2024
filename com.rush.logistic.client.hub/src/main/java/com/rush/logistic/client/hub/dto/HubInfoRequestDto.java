package com.rush.logistic.client.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubInfoRequestDto {
    private String name;
    private String address;
}
