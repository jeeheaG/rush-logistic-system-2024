package com.rush.logistic.client.slack.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackInfoListResponseDto<T> {
    private List<T> slackList;

    public static <T> SlackInfoListResponseDto<T> of(List<T> slackList) {
        return SlackInfoListResponseDto.<T>builder()
                .slackList(slackList)
                .build();
    }
}