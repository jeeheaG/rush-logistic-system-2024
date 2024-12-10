package com.rush.logistic.client.order_delivery.global.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.rush.logistic.client.order_delivery.global.common.GlobalConst.TIME_ZONE_ID;

// TODO : deletedAt, deletedBy 삭제 시 soft delete 로직

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseAudit {
    @Column(updatable = false)
    protected ZonedDateTime createdAt;

    protected ZonedDateTime updatedAt;

    protected ZonedDateTime deletedAt;

    protected boolean isDelete = false;

    @CreatedBy
    @Column(updatable = false) // TODO : nullable = false,
    protected UUID createdBy;

    @LastModifiedBy
    protected UUID updatedBy;

    protected UUID deletedBy;

    @PrePersist
    protected void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
        this.updatedAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
    }

    protected void setDeleteInfo(UUID deleteUserId) {
        this.deletedAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
        this.deletedBy = deleteUserId;
//        this.isDelete = true; // @SQLDelete 로 대체
    }
}
