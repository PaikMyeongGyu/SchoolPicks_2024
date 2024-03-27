package org.schoolPicks.domain.repository.shop;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.repository.shop.condition.ShopFindCond;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

import static org.schoolPicks.domain.entity.shop.QShop.shop;


@Repository
@RequiredArgsConstructor
public class ShopQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    private Random random = new Random();

    public Shop findShop(ShopFindCond cond){

        // 모든 데이터를
        Long shopCount = getShopCount(cond);

        if(shopCount == 0)
            return null;

        int shopIndex = random.nextInt(Math.toIntExact(shopCount));
        Shop findShop = findShopQuery(cond)
                .offset(shopIndex)
                .limit(1)
                .fetchOne();

        return findShop;
    }

    public List<Shop> findAll(ShopFindCond dto){
        return findShopQuery(dto)
                .fetch();
    }

    private Long getShopCount(ShopFindCond dto) {
        Long shopCount = queryFactory
                .select(shop.count())
                .from(shop)
                .where(
                        priceBetween(dto),
                        schoolTypesEq(dto),
                        shopTypesIn(dto),
                        timeAfterOpenTimeAndBeforeCloseTime(dto)
                )
                .fetchOne();
        return shopCount;
    }

    private JPAQuery<Shop> findShopQuery(ShopFindCond dto) {
        return queryFactory
                .select(shop)
                .from(shop)
                .where(
                        priceBetween(dto),
                        schoolTypesEq(dto),
                        shopTypesIn(dto),
                        timeAfterOpenTimeAndBeforeCloseTime(dto)
                );
    }

    private BooleanExpression shopTypesIn(ShopFindCond dto) {
        if(dto.getShopTypes() == null)
            return null;
        return shop.shopType.in(dto.getShopTypes());
    }

    private BooleanExpression schoolTypesEq(ShopFindCond dto) {
        if(dto.getSchoolType() == null){
            return null;
        }
        return shop.schoolType.eq(dto.getSchoolType());
    }

    private BooleanExpression priceBetween(ShopFindCond dto) {
        if(dto.getPriceMin() == null || dto.getPriceMax() == null)
            return null;
        return shop.price.between(dto.getPriceMin(), dto.getPriceMax());
    }

    private BooleanExpression timeAfterOpenTimeAndBeforeCloseTime(ShopFindCond dto){
        if(dto.getCurrentTime() == null){
            return null;
        }
        return shop.openTime.after(dto.getCurrentTime())
                .and(shop.closeTime.before(dto.getCurrentTime()));
    }
}
