package com.rush.logistic.client.domain.global;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private String createdBy;

    @LastModifiedDate
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "update_by")
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete;

    public BaseEntity(){
        this.isDelete = false;
    }

    @PreUpdate
    public void updateDelete(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String userId = Objects.requireNonNull(attributes).getRequest().getHeader("USER_ID");
        if(this.isDelete()) {
            this.setDeletedBy(userId);
            this.setDeletedAt(LocalDateTime.now());
        }
        this.setUpdatedBy(userId);
        this.setUpdatedAt(LocalDateTime.now());
    }
}