package com.rush.logistic.client.hub.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubListResponseDto<T> {
    private List<T> hubList;

    public static <T> HubListResponseDto<T> from(List<T> hubList) {
        return HubListResponseDto.<T>builder()
            .hubList(hubList)
            .build();
    }
}
