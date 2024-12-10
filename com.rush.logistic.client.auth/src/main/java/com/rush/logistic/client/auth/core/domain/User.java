package com.rush.logistic.client.auth.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    private String userId;
    private String username;
    private String password;
    private String nickname;
    private String slackId;
    private String role;
}
