package com.rush.logistic.client.slack.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackInfoResponseDto {

    private String slackId;
    private String message;
    private LocalDateTime createdAt;

    public static SlackInfoResponseDto from(String slackId, String message) {
        return SlackInfoResponseDto.builder()
                .slackId(slackId)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
    }
}