package com.rush.logistic.client.domain.global;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "create_by", updatable = false)
    private Long createdBy;

    @LastModifiedDate
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "update_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete;

//    @PrePersist
//    public void prePersist() {
//        if (this.createdAt == null) {
//            this.createdAt = LocalDateTime.now();
//        }
//        if (this.createdBy == null) {
//            this.createdBy = Long.parseLong(getUser()); // 현재 로그인한 사용자 설정
//        }
//    }
//
//    private String getUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            return userDetails.getUsername(); // 사용자 이름을 반환 (예: 이메일, ID 등)
//        }
//        return "anonymous"; // 인증되지 않은 경우 기본값 설정
//    }

//    @PreUpdate
//    public void updateDelete(){
//        if(this.isDelete()) {
//            this.setDeletedAt(LocalDateTime.now());
//        }
//        this.setUpdatedAt(LocalDateTime.now());
//    }
}