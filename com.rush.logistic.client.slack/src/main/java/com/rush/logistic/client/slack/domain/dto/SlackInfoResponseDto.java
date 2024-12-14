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
    private String sendUserId;
    private String receiveUserId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public static SlackInfoResponseDto from(SlackEntity slack) {
        return SlackInfoResponseDto.builder()
                .slackId(slack.getSlackId())
                .message(slack.getMessage())
                .sendUserId(slack.getSendUserId())
                .receiveUserId(slack.getReceiveUserSlackId())
                .createdAt(slack.getCreatedAt())
                .createdBy(slack.getCreatedBy())
                .updatedAt(slack.getUpdatedAt())
                .updatedBy(slack.getUpdatedBy())
                .build();
    }
}