package com.example.demo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = 2005132698L;

    public static final QReply reply = new QReply("reply");

    public final StringPath content = createString("content");

    public final StringPath id = createString("id");

    public final StringPath msg = createString("msg");

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final NumberPath<Integer> rnum = createNumber("rnum", Integer.class);

    public QReply(String variable) {
        super(Reply.class, forVariable(variable));
    }

    public QReply(Path<? extends Reply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReply(PathMetadata metadata) {
        super(Reply.class, metadata);
    }

}

