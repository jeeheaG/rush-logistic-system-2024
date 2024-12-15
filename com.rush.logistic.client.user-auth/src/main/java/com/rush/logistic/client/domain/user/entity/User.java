package com.rush.logistic.client.domain.user.entity;

import com.rush.logistic.client.domain.global.BaseEntity;
import com.rush.logistic.client.domain.user.dto.UserUpdateRequestDto;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
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
@Table(name = "p_users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    //

    @Column(name = "hub_id", unique = true)
    private UUID hubId;

    @Column(name = "company_id", unique = true)
    private UUID companyId;

    public void updateUser(UserUpdateRequestDto requestDto){

        Optional.ofNullable(requestDto.getUsername()).ifPresent(username -> this.username = username);
        Optional.ofNullable(requestDto.getRole()).ifPresent(role -> this.role = role);
        Optional.ofNullable(requestDto.getEmail()).ifPresent(email -> this.email = email);
        Optional.ofNullable(requestDto.getHubId()).ifPresent(huhId -> this.hubId = huhId);
        Optional.ofNullable(requestDto.getCompanyId()).ifPresent(companyId -> this.companyId = companyId);
    }
}
