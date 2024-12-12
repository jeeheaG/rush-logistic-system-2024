package com.rush.logistic.client.domain.user.entity;

import com.rush.logistic.client.domain.global.BaseEntity;
import com.rush.logistic.client.domain.user.dto.UserUpdateRequestDto;
import com.rush.logistic.client.domain.user.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;


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
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public void updateUser(UserUpdateRequestDto requestDto){

        Optional.ofNullable(requestDto.getUsername()).ifPresent(username -> this.username = username);
        Optional.ofNullable(requestDto.getSlackId()).ifPresent(slackId -> this.slackId = slackId);
        Optional.ofNullable(requestDto.getRole()).ifPresent(role -> this.role = role);
        Optional.ofNullable(requestDto.getEmail()).ifPresent(role -> this.email = email);
    }

    // TODO : 로그인한 유저 받아서 넣기
//    @PrePersist
//    public void createField(){
//        this.setCreatedBy(getUserId());
//    }

//    private static String getUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetailsImpl) {
//            return userDetailsImpl.getUser().getUsername();
//        }
//        return null;
//    }

    // TODO : 로그인한 유저
    @PreUpdate
    public void updateDeleteField(){
        if(this.isDelete()) {
            this.setDeletedAt(LocalDateTime.now());
//            this.setDeletedBy(getUserName);
        }
        this.setUpdatedAt(LocalDateTime.now());
//        this.setUpdatedBy(getUserName());
    }


}
