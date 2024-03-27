package org.schoolPicks.domain.entity.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShop is a Querydsl query type for Shop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShop extends EntityPathBase<Shop> {

    private static final long serialVersionUID = -1181933549L;

    public static final QShop shop = new QShop("shop");

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final StringPath menuDescription = createString("menuDescription");

    public final StringPath name = createString("name");

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<SchoolType> schoolType = createEnum("schoolType", SchoolType.class);

    public final EnumPath<ShopType> shopType = createEnum("shopType", ShopType.class);

    public final StringPath url = createString("url");

    public final StringPath xPosition = createString("xPosition");

    public final StringPath yPosition = createString("yPosition");

    public QShop(String variable) {
        super(Shop.class, forVariable(variable));
    }

    public QShop(Path<? extends Shop> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShop(PathMetadata metadata) {
        super(Shop.class, metadata);
    }

}

