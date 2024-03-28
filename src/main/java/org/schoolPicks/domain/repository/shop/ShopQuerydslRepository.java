package org.schoolPicks.domain.repository.shop;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.repository.shop.condition.ShopSearchCond;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

import static org.schoolPicks.domain.entity.shop.QShop.shop;


    @Repository
    @RequiredArgsConstructor
    public class ShopQuerydslRepository {

        private final JPAQueryFactory queryFactory;

        public List<Shop> findAllBySearchCond(ShopSearchCond dto){
            return queryFactory
                    .select(shop)
                    .from(shop)
                    .where(
                            priceBetween(dto),
                            schoolTypesEq(dto),
                            shopTypesIn(dto),
                            timeAfterOpenTimeAndBeforeCloseTime(dto)
                    )
                    .fetch();
        }

        private Long findShopCount(ShopSearchCond dto) {
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

        private BooleanExpression shopTypesIn(ShopSearchCond dto) {
            if(dto.getShopTypes() == null)
                return null;
            return shop.shopType.in(dto.getShopTypes());
        }

        private BooleanExpression schoolTypesEq(ShopSearchCond dto) {
            if(dto.getSchoolType() == null){
                return null;
            }
            return shop.schoolType.eq(dto.getSchoolType());
        }

        private BooleanExpression priceBetween(ShopSearchCond dto) {
            if(dto.getPriceMin() == null || dto.getPriceMax() == null)
                return null;
            return shop.price.between(dto.getPriceMin(), dto.getPriceMax());
        }

        private BooleanExpression timeAfterOpenTimeAndBeforeCloseTime(ShopSearchCond dto){
            if(dto.getCurrentTime() == null){
                return null;
            }
            return shop.openTime.before(dto.getCurrentTime())
                    .and(shop.closeTime.after(dto.getCurrentTime()));
        }
    }
