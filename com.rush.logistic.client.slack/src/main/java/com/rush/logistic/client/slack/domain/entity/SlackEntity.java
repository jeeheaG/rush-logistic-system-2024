package com.rush.logistic.client.slack.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_slacks")
public class SlackEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slackId;

    @Column(name = "message")
    private String message;

    @Column(name = "sendUserId")
    private String sendUserId;

    @Column(name = "receiveUserSlackId")
    private String receiveUserSlackId;
}