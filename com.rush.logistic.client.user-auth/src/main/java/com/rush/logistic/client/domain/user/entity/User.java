package com.rush.logistic.client.domain.user.entity;

import com.rush.logistic.client.domain.global.BaseEntity;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

//    @Column(name = "nickname", nullable = false, unique = true)
//    private String nickname;

    @Column(name = "slackId", nullable = false, unique = true)
    private String slackId;

    @Column(name = "role", nullable = false)
    private UserRoleEnum role;
}
