package org.schoolPicks.domain.repository.shop;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.repository.shop.condition.ShopSearchCond;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.schoolPicks.domain.entity.shop.QShop.shop;


    @Repository
    @RequiredArgsConstructor
    public class ShopQuerydslRepository {

        private final JPAQueryFactory queryFactory;

        public List<Shop> findAllBySearchCond(ShopSearchCond cond){
            return queryFactory
                    .select(shop)
                    .from(shop)
                    .where(
                            priceBetween(cond),
                            schoolTypesEq(cond),
                            shopTypesIn(cond),
                            timeInOpeningHours(cond)
                    )
                    .fetch();
        }

        public Shop findShopBySearchCondAndOffset(ShopSearchCond cond, int offset){
            return queryFactory
                    .select(shop)
                    .from(shop)
                    .where(
                            priceBetween(cond),
                            schoolTypesEq(cond),
                            shopTypesIn(cond),
                            timeInOpeningHours(cond)
                    )
                    .offset(offset)
                    .limit(1L)
                    .fetchOne();
        }

        public Long findShopCount(ShopSearchCond cond) {
            Long shopCount = queryFactory
                    .select(shop.count())
                    .from(shop)
                    .where(
                            priceBetween(cond),
                            schoolTypesEq(cond),
                            shopTypesIn(cond),
                            timeInOpeningHours(cond)
                    )
                    .fetchOne();
            return shopCount;
        }

        private BooleanExpression shopTypesIn(ShopSearchCond cond) {
            if(cond.getShopTypes() == null)
                return null;
            return shop.shopType.in(cond.getShopTypes());
        }

        private BooleanExpression schoolTypesEq(ShopSearchCond cond) {
            if(cond.getSchoolType() == null){
                return null;
            }
            return shop.schoolType.eq(cond.getSchoolType());
        }

        private BooleanExpression priceBetween(ShopSearchCond cond) {
            if(cond.getPriceMin() == null || cond.getPriceMax() == null)
                return null;
            return shop.price.between(cond.getPriceMin(), cond.getPriceMax());
        }

        private BooleanExpression timeInOpeningHours(ShopSearchCond cond){
            if(cond.getCurrentTime() == null){
                return null;
            }

            BooleanExpression betweenOpenAndCloseTime = shop.openTime.before(cond.getCurrentTime())
                    .and(shop.closeTime.after(cond.getCurrentTime()));

            // 오픈 시간은 오후 5시인데 영업 종료시간은 새벽인 경우를 위한 처리
            BooleanExpression afterOpenAndBeforeCloseTime = shop.openTime.after(shop.closeTime)
                    .and(shop.openTime.before(cond.getCurrentTime()).or(shop.closeTime.after(cond.getCurrentTime())));

            return betweenOpenAndCloseTime.or(afterOpenAndBeforeCloseTime);
        }
    }
