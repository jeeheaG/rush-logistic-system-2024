package com.rush.logistic.client.slack.domain.dto;

import com.rush.logistic.client.slack.domain.entity.SlackEntity;
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

    private Long slackId;
    private String message;
    private LocalDateTime createdAt;
    private String sendUserId;
    private String receiveUserId;

    public static SlackInfoResponseDto of(SlackEntity slack) {
        return SlackInfoResponseDto.builder()
                .slackId(slack.getSlackId())
                .message(slack.getMessage())
                .createdAt(slack.getCreatedAt())
                .sendUserId(slack.getSendUserId())
                .receiveUserId(slack.getReceiveUserSlackId())
                .build();
    }
}