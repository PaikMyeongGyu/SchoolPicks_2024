package org.schoolPicks.repository;

import org.junit.jupiter.api.*;
import org.schoolPicks.domain.entity.shop.SchoolType;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.entity.shop.ShopType;
import org.schoolPicks.domain.repository.shop.condition.ShopSearchCond;
import org.schoolPicks.domain.repository.shop.ShopQuerydslRepository;
import org.schoolPicks.domain.repository.shop.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.schoolPicks.domain.entity.shop.SchoolType.*;
import static org.schoolPicks.domain.entity.shop.ShopType.*;

@SpringBootTest
class ShopQuerydslRepositoryTest {

    @Autowired
    ShopQuerydslRepository shopQuerydslRepository;

    @Autowired
    ShopRepository shopRepository;


    @AfterEach
    void tearDown(){
        shopRepository.deleteAllInBatch();
    }

    @DisplayName("학교 종류는 단 한 개만 선택해서 조회할 수 있다.")
    @Test
    void findBySchoolType(){
        // given
        Shop shop1 = createShopWithOutTime("식당1", 1000, RESTAURANT, NSC);
        Shop shop2 = createShopWithOutTime("식당2", 2000, CAFE, NSC);
        Shop shop3 = createShopWithOutTime("식당3", 3000, PUB, NSC);
        Shop shop4 = createShopWithOutTime("식당4", 1000, RESTAURANT, HSSC);
        Shop shop5 = createShopWithOutTime("식당5", 2000, CAFE, HSSC);
        Shop shop6 = createShopWithOutTime("식당6", 3000, PUB, HSSC);
        shopRepository.saveAll(List.of(shop1, shop2, shop3, shop4, shop5, shop6));

        ShopSearchCond cond = ShopSearchCond.builder()
                .schoolType(NSC)
                .build();

        // when
        List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

        assertThat(shops).hasSize(3)
                .extracting("name", "price", "schoolType")
                .containsExactlyInAnyOrder(
                        tuple("식당1", 1000, NSC),
                        tuple("식당2", 2000, NSC),
                        tuple("식당3", 3000, NSC)
                );
    }

    @DisplayName("설정한 최솟값과 최댓값으로 범위 내의 식당을 찾을 수 있다.")
    @Test
    void findByPrice(){
        // given
        Shop shop1 = createShopWithOutTime("식당1", 1000, RESTAURANT, NSC);
        Shop shop2 = createShopWithOutTime("식당2", 2000, CAFE, NSC);
        Shop shop3 = createShopWithOutTime("식당3", 3000, PUB, NSC);
        shopRepository.saveAll(List.of(shop1, shop2, shop3));

        ShopSearchCond cond = ShopSearchCond.builder()
                .priceMin(1000)
                .priceMax(2000)
                .build();

        // when
        List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

        assertThat(shops).hasSize(2)
                .extracting("name", "price", "schoolType")
                .containsExactlyInAnyOrder(
                        tuple("식당1", 1000, NSC),
                        tuple("식당2", 2000, NSC)
                );
    }

    @DisplayName("영업 시간으로 식당을 조회할 수 있다.")
    @TestFactory
    Collection<DynamicTest> findByTimeDynamicTest(){
        // given
        Shop shop1 = createShop("식당1", 0, null, null,
                LocalTime.of(1, 0), LocalTime.of(8, 0));
        Shop shop2 = createShop("식당2", 0, null, null,
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        Shop shop3 = createShop("식당3", 0, null, null,
                LocalTime.of(16, 0), LocalTime.of(23, 0));
        shopRepository.saveAll(List.of(shop1, shop2, shop3));


        return List.of(
                DynamicTest.dynamicTest("식당 운영 시간에는 식당이 검색된다.", () ->{
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .currentTime(LocalTime.of(5, 0))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

                    assertThat(shops).hasSize(1)
                            .extracting("name", "price")
                            .containsExactlyInAnyOrder(
                                    tuple("식당1", 0)
                            );
                }),
                DynamicTest.dynamicTest("식당 운영 시작 시간과 운영 종료 시간엔 식당이 검색되지 않는다.", () ->{
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .currentTime(LocalTime.of(8, 0))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

                    assertThat(shops).hasSize(0);
                })
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
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .shopTypes(List.of(RESTAURANT))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

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
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .shopTypes(List.of(RESTAURANT, CAFE))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

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
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .shopTypes(List.of(RESTAURANT, CAFE, PUB))
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

                    // then
                    assertThat(shops).hasSize(6);
                }),
                DynamicTest.dynamicTest("식당 종류를 선택하지 않은 경우, 전체를 선택한다.", ()->{
                    // given
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .shopTypes(null)
                            .build();

                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);

                    // then
                    assertThat(shops).hasSize(6);
                })
        );
    }

    @DisplayName("식당 종류, 학교 종류, 가격, 시간으로 식당을 검색할 수 있다.")
    @TestFactory
    Collection<DynamicTest> findByShopSearchCondDynamicTest(){
        // given
        Shop shop1 = createShop("식당1", 1000, RESTAURANT, NSC,
                LocalTime.of(1, 0), LocalTime.of(8, 0));
        Shop shop2 = createShop("식당2", 2000, RESTAURANT, NSC,
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        Shop shop3 = createShop("식당3", 3000, RESTAURANT, NSC,
                LocalTime.of(1, 0), LocalTime.of(8, 0));
        Shop shop4 = createShop("식당4", 3000, RESTAURANT, NSC,
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        Shop shop5 = createShop("식당5", 3000, PUB, NSC,
                LocalTime.of(16, 0), LocalTime.of(23, 0));
        Shop shop6 = createShop("식당6", 3000, PUB, HSSC,
                LocalTime.of(16, 0), LocalTime.of(23, 0));
        shopRepository.saveAll(List.of(shop1, shop2, shop3, shop4, shop5, shop6));


        return List.of(
                DynamicTest.dynamicTest("식당이 속한 학교 종류와 식당 종류로 식당을 검색할 수 있다.", () ->{
                    // given
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .schoolType(NSC)
                            .shopTypes(List.of(RESTAURANT))
                            .build();
                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);
                    // then
                    assertThat(shops).hasSize(4)
                            .extracting("name", "price")
                            .containsExactlyInAnyOrder(
                                    tuple("식당1", 1000),
                                    tuple("식당2", 2000),
                                    tuple("식당3", 3000),
                                    tuple("식당4", 3000)
                            );
                }),
                DynamicTest.dynamicTest("식당이 속한 학교 종류, 식당 종류, 가격으로 식당을 검색할 수 있다. ", () ->{
                    // given
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .schoolType(NSC)
                            .shopTypes(List.of(RESTAURANT))
                            .priceMin(2500)
                            .priceMax(3500)
                            .build();
                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);
                    // then
                    assertThat(shops).hasSize(2)
                            .extracting("name", "price")
                            .containsExactlyInAnyOrder(
                                    tuple("식당3", 3000),
                                    tuple("식당4", 3000)
                            );
                }),
                DynamicTest.dynamicTest("식당이 속한 학교 종류, 식당 종류, 가격, 운영시간으로 식당을 검색할 수 있다. ", () ->{
                    // given
                    ShopSearchCond cond = ShopSearchCond.builder()
                            .schoolType(NSC)
                            .shopTypes(List.of(RESTAURANT))
                            .priceMin(2500)
                            .priceMax(3500)
                            .currentTime(LocalTime.of(13, 0))
                            .build();
                    // when
                    List<Shop> shops = shopQuerydslRepository.findAllBySearchCond(cond);
                    // then
                    assertThat(shops).hasSize(1)
                            .extracting("name", "price")
                            .containsExactlyInAnyOrder(
                                    tuple("식당4", 3000)
                            );
                })
        );
    }

    private Shop createShop(String name, int price, ShopType shopType, SchoolType schoolType,
                            LocalTime openTime, LocalTime closeTime) {
        return Shop.builder()
                .name(name)
                .price(price)
                .shopType(shopType)
                .schoolType(schoolType)
                .openTime(openTime)
                .closeTime(closeTime)
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