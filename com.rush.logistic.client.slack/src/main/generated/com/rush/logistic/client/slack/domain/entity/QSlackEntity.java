package com.rush.logistic.client.slack.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSlackEntity is a Querydsl query type for SlackEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSlackEntity extends EntityPathBase<SlackEntity> {

    private static final long serialVersionUID = -1217813546L;

    public static final QSlackEntity slackEntity = new QSlackEntity("slackEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final NumberPath<Long> deletedBy = _super.deletedBy;

    //inherited
    public final BooleanPath isDelete = _super.isDelete;

    public final StringPath message = createString("message");

    public final StringPath password = createString("password");

    public final StringPath receiveUserSlackId = createString("receiveUserSlackId");

    public final StringPath sendUserId = createString("sendUserId");

    public final NumberPath<Long> slackId = createNumber("slackId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QSlackEntity(String variable) {
        super(SlackEntity.class, forVariable(variable));
    }

    public QSlackEntity(Path<? extends SlackEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSlackEntity(PathMetadata metadata) {
        super(SlackEntity.class, metadata);
    }

}

