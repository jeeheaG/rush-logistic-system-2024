package com.rush.logistic.client.slack.domain.dto;

import com.rush.logistic.client.slack.domain.entity.SlackEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackInfoResponseDto {

    private UUID Id;
    private String message;
    private String sendUserId;
    private String receiveUserSlackId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public static SlackInfoResponseDto from(SlackEntity slack) {
        return SlackInfoResponseDto.builder()
                .Id(slack.getId())
                .message(slack.getMessage())
                .sendUserId(slack.getSendUserId())
                .receiveUserSlackId(slack.getReceiveUserSlackId())
                .createdAt(slack.getCreatedAt())
                .createdBy(slack.getCreatedBy())
                .updatedAt(slack.getUpdatedAt())
                .updatedBy(slack.getUpdatedBy())
                .build();
    }
}