package com.rush.logistic.client.slack.domain.entity;


import com.rush.logistic.client.slack.domain.dto.SlackUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_slacks")
public class SlackEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "slack_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "message")
    private String message;

    @Column(name = "send_User_Id")
    private String sendUserId;

    @Column(name = "receive_User_Slack_Id")
    private String receiveUserSlackId;

    public void updateSlackEntity(SlackUpdateRequestDto requestDto){

        Optional.ofNullable(requestDto.getMessage()).ifPresent(message -> this.message = message);
        Optional.ofNullable(requestDto.getReceiveUserSlackId()).ifPresent(sendUserId -> this.sendUserId = sendUserId);
        Optional.ofNullable(requestDto.getSendUserId()).ifPresent(receiveUserSlackId -> this.receiveUserSlackId = receiveUserSlackId);
    }
}