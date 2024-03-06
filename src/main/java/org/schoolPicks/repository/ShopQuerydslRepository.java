package org.schoolPicks.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.schoolPicks.entity.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

import static org.schoolPicks.entity.QShop.*;

@Repository
@RequiredArgsConstructor
public class ShopQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    private Random random = new Random();

    public Shop findShopPaging(ShopQuerydslDto dto){

        // 모든 데이터를
        Long shopCount = getShopCount(dto);
        if(shopCount == 0)
            return null;

        int shopIndex = random.nextInt(Math.toIntExact(shopCount));
        Shop findShop = findShopQuery(dto)
                .offset(shopIndex)
                .limit(1)
                .fetchOne();

        return findShop;
    }

    public List<Shop> findAll(ShopQuerydslDto dto){
        return findShopQuery(dto)
                .fetch();
    }

    private Long getShopCount(ShopQuerydslDto dto) {
        Long shopCount = queryFactory
                .select(shop.count())
                .from(shop)
                .where(
                        priceBetween(dto),
                        schoolTypesEq(dto),
                        shopTypesIn(dto)
                )
                .fetchOne();
        return shopCount;
    }

    private JPAQuery<Shop> findShopQuery(ShopQuerydslDto dto) {
        return queryFactory
                .select(shop)
                .from(shop)
                .where(
                        priceBetween(dto),
                        schoolTypesEq(dto),
                        shopTypesIn(dto)
                );
    }

    private static BooleanExpression shopTypesIn(ShopQuerydslDto dto) {
        if(dto.getShopTypes() == null)
            return null;
        return shop.shopType.in(dto.getShopTypes());
    }

    private static BooleanExpression schoolTypesEq(ShopQuerydslDto dto) {
        if(dto.getSchoolType() == null){
            return null;
        }
        return shop.schoolType.eq(dto.getSchoolType());
    }

    private static BooleanExpression priceBetween(ShopQuerydslDto dto) {
        if(dto.getPriceMin() == null || dto.getPriceMax() == null)
            return null;
        return shop.price.between(dto.getPriceMin(), dto.getPriceMax());
    }
}
