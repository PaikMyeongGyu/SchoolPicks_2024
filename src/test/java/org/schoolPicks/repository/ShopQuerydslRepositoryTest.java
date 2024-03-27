package org.schoolPicks.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.schoolPicks.domain.entity.shop.SchoolType;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.entity.shop.ShopType;
import org.schoolPicks.domain.repository.shop.condition.ShopFindCond;
import org.schoolPicks.domain.repository.shop.ShopQuerydslRepository;
import org.schoolPicks.domain.repository.shop.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.schoolPicks.domain.entity.shop.SchoolType.*;
import static org.schoolPicks.domain.entity.shop.ShopType.*;

@Transactional
@SpringBootTest
class ShopQuerydslRepositoryTest {

    @Autowired
    ShopQuerydslRepository shopQuerydslRepository;

    @Autowired
    ShopRepository shopRepository;

    @DisplayName("학교 종류는 단 한 개만 선택해서 조회할 수 있다.")
    @Test
    void findBySchoolTypeDynamicTest(){
        // given
        Shop shop1 = createShopWithOutTime("식당1", 1000, RESTAURANT, NSC);
        Shop shop2 = createShopWithOutTime("식당2", 2000, CAFE, NSC);
        Shop shop3 = createShopWithOutTime("식당3", 3000, PUB, NSC);
        Shop shop4 = createShopWithOutTime("식당4", 1000, RESTAURANT, HSSC);
        Shop shop5 = createShopWithOutTime("식당5", 2000, CAFE, HSSC);
        Shop shop6 = createShopWithOutTime("식당6", 3000, PUB, HSSC);
        shopRepository.saveAll(List.of(shop1, shop2, shop3, shop4, shop5, shop6));

        ShopFindCond cond = ShopFindCond.builder()
                .schoolType(NSC)
                .build();

        // when
        List<Shop> shops = shopQuerydslRepository.findAll(cond);

        assertThat(shops).hasSize(3)
                .extracting("name", "price", "schoolType")
                .containsExactlyInAnyOrder(
                        tuple("식당1", 1000, NSC),
                        tuple("식당2", 2000, NSC),
                        tuple("식당3", 3000, NSC)
                );
    }

    @DisplayName("식당 종류는 여러 개를 동시에 선택해서 조회할 수 있다.")
    @TestFactory
    Collection<DynamicTest> findByShopTypeDynamicTest(){
        // given
        Shop shop1 = createShopWithOutTime("식당1", 1000, RESTAURANT, NSC);
        Shop shop2 = createShopWithOutTime("식당2", 2000, CAFE, NSC);
        Shop shop3 = createShopWithOutTime("식당3", 3000, PUB, NSC);
        Shop shop4 = createShopWithOutTime("식당4", 1000, RESTAURANT, HSSC);
        Shop shop5 = createShopWithOutTime("식당5", 2000, CAFE, HSSC);
        Shop shop6 = createShopWithOutTime("식당6", 3000, PUB, HSSC);
        shopRepository.saveAll(List.of(shop1, shop2, shop3, shop4, shop5, shop6));


        // when // then
        return List.of(
                DynamicTest.dynamicTest("식당 타입 1개로 조회", () ->{
                    // given
                    ShopFindCond cond = ShopFindCond.builder()
                            .shopTypes(List.of(RESTAURANT))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAll(cond);

                    // then
                    assertThat(shops).hasSize(2)
                            .extracting("name", "price", "shopType")
                            .containsExactlyInAnyOrder(
                                    tuple("식당1", 1000, RESTAURANT),
                                    tuple("식당4", 1000, RESTAURANT)
                            );
                }),
                DynamicTest.dynamicTest("식당 타입 2개로 조회", () ->{
                    // given
                    ShopFindCond cond = ShopFindCond.builder()
                            .shopTypes(List.of(RESTAURANT, CAFE))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAll(cond);

                    // then
                    assertThat(shops).hasSize(4)
                            .extracting("name", "price", "shopType")
                            .containsExactlyInAnyOrder(
                                    tuple("식당1", 1000, RESTAURANT),
                                    tuple("식당4", 1000, RESTAURANT),
                                    tuple("식당2", 2000, CAFE),
                                    tuple("식당5", 2000, CAFE)
                            );
                }),
                DynamicTest.dynamicTest("식당 타입 3개로 조회", ()->{
                    // given
                    ShopFindCond cond = ShopFindCond.builder()
                            .shopTypes(List.of(RESTAURANT, CAFE, PUB))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAll(cond);

                    // then
                    assertThat(shops).hasSize(6);
                }),
                DynamicTest.dynamicTest("식당 종류를 선택하지 않은 경우, 전체를 선택한다.", ()->{
                    // given
                    ShopFindCond cond = ShopFindCond.builder()
                            .shopTypes(null)
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAll(cond);

                    // then
                    assertThat(shops).hasSize(6);
                })
        );
    }



//    @Test
//    void 학교별_검색확인(){
//        List<Shop> findNSCShop = shopQuerydslRepository
//                .findAll(new ShopFindCond(NSC, null, null, null));
//        assertThat(findNSCShop.size()).isEqualTo(3);
//
//        List<Shop> findHSSCShop = shopQuerydslRepository
//                .findAll(new ShopFindCond(HSSC, null, null, null));
//        assertThat(findHSSCShop.size()).isEqualTo(3);
//    }
//
//    @Test
//    void 데이터전체_검색확인(){
//        List<Shop> findShops = shopQuerydslRepository
//                .findAll(new ShopFindCond(null, null, null, null));
//        assertThat(findShops.size()).isEqualTo(6);
//    }
//
//    @Test
//    void 식당타입_개수별_검색확인(){
//        List<Shop> findShops = shopQuerydslRepository
//                .findAll(new ShopFindCond(null, List.of(CAFE), null, null));
//        assertThat(findShops.size()).isEqualTo(2);
//
//        List<Shop> findShops2 = shopQuerydslRepository
//                .findAll(new ShopFindCond(null, List.of(CAFE, RESTAURANT), null, null));
//        assertThat(findShops2.size()).isEqualTo(4);
//
//        List<Shop> findShops3 = shopQuerydslRepository
//                .findAll(new ShopFindCond(null, List.of(CAFE, RESTAURANT, PUB), null, null));
//        assertThat(findShops3.size()).isEqualTo(6);
//    }
//
//    @Test
//    void 식당가격별_검색확인(){
//        List<Shop> findShops1 = shopQuerydslRepository
//                .findAll(new ShopFindCond(null, null, 1000, 3000));
//        assertThat(findShops1.size()).isEqualTo(3);
//
//        List<Shop> findShops2 = shopQuerydslRepository
//                .findAll(new ShopFindCond(null, null, 1000, 6000));
//        assertThat(findShops2.size()).isEqualTo(6);
//
//        List<Shop> findShops3 = shopQuerydslRepository
//                .findAll(new ShopFindCond(null, null, 10000, 20000));
//        assertThat(findShops3.size()).isEqualTo(0);
//    }
//
//    @Test
//    void 복합조건_검사(){
//        List<Shop> findShops1 = shopQuerydslRepository
//                .findAll(new ShopFindCond(NSC, null, 1000, 2000));
//        assertThat(findShops1.size()).isEqualTo(2);
//
//        List<Shop> findShops2 = shopQuerydslRepository
//                .findAll(new ShopFindCond(NSC, List.of(CAFE, PUB), null, null));
//        assertThat(findShops2.size()).isEqualTo(2);
//
//        List<Shop> findShops3 = shopQuerydslRepository
//                .findAll(new ShopFindCond(NSC, List.of(CAFE, PUB, RESTAURANT), 0, 1000));
//        assertThat(findShops3.size()).isEqualTo(1);
//    }

    private Shop createShop(String name, int price, ShopType shopType, SchoolType schoolType,
                            LocalTime openTime, LocalTime closeTime) {
        return Shop.builder()
                .name(name)
                .price(price)
                .shopType(shopType)
                .schoolType(schoolType)
                .openTime(openTime)
                .closeTime(openTime)
                .build();
    }

    private Shop createShopWithOutTime(String name, int price, ShopType shopType, SchoolType schoolType) {
        return Shop.builder()
                .name(name)
                .price(price)
                .shopType(shopType)
                .schoolType(schoolType)
                .build();
    }
}