package org.schoolPicks.api.service.shop;

import org.junit.jupiter.api.*;
import org.schoolPicks.api.service.shop.request.ShopSearchServiceRequest;
import org.schoolPicks.domain.entity.shop.SchoolType;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.entity.shop.ShopType;
import org.schoolPicks.domain.repository.shop.ShopRepository;
import org.schoolPicks.domain.repository.shop.condition.ShopSearchCond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.schoolPicks.domain.entity.shop.SchoolType.HSSC;
import static org.schoolPicks.domain.entity.shop.SchoolType.NSC;
import static org.schoolPicks.domain.entity.shop.ShopType.PUB;
import static org.schoolPicks.domain.entity.shop.ShopType.RESTAURANT;

@SpringBootTest
class ShopServiceTest {

    @Autowired
    ShopService shopService;

    @Autowired
    ShopRepository shopRepository;

    @AfterEach
    void tearDown(){
        shopRepository.deleteAllInBatch();
    }

    @DisplayName("선택한 옵션에 따른 식당은 단 1개만 선택할 수 있다.")
    @TestFactory
    Collection<DynamicTest> findShopTest(){
        // given
        Shop shop1 = createShop("식당1", 1000, RESTAURANT, NSC,
                LocalTime.of(17, 0), LocalTime.of(20, 0));
        Shop shop2 = createShop("식당2", 2000, RESTAURANT, NSC,
                LocalTime.of(17, 0), LocalTime.of(23, 0));
        Shop shop3 = createShop("식당3", 3000, RESTAURANT, NSC,
                LocalTime.of(17, 0), LocalTime.of(0, 0));
        Shop shop4 = createShop("식당4", 3000, RESTAURANT, NSC,
                LocalTime.of(17, 0), LocalTime.of(8, 0));
        Shop shop5 = createShop("식당5", 3000, PUB, HSSC,
                LocalTime.of(17, 0), LocalTime.of(23, 0));
        Shop shop6 = createShop("식당6", 3000, PUB, HSSC,
                LocalTime.of(17, 0), LocalTime.of(5, 0));
        shopRepository.saveAll(List.of(shop1, shop2, shop3, shop4, shop5, shop6));

        return List.of(
                DynamicTest.dynamicTest("원하는 조건에 따른 식당을 1개 가지고 온다.", () ->{
                    ShopSearchServiceRequest request = createFindShopRequest(List.of(RESTAURANT), NSC, LocalTime.of(22,0));

                    // when
                    Shop findShop = shopService.findRandomShop(request, 0);
                    // then
                    assertThat(findShop)
                            .extracting("name", "price")
                            .containsExactly(
                                    "식당2", 2000
                            );
                }),
                DynamicTest.dynamicTest("원하는 조건에 따른 식당을 1개 가지고 온다.", () ->{
                    ShopSearchServiceRequest request = createFindShopRequest(List.of(RESTAURANT), NSC, LocalTime.of(22,0));

                    // when
                    Shop findShop = shopService.findRandomShop(request, 1);
                    // then
                    assertThat(findShop)
                            .extracting("name", "price")
                            .containsExactly(
                                    "식당3", 3000
                            );
                }),
                DynamicTest.dynamicTest("원하는 조건에 따른 식당을 1개 가지고 온다.", () ->{
                    ShopSearchServiceRequest request = createFindShopRequest(List.of(RESTAURANT), NSC, LocalTime.of(22,0));

                    // when
                    Shop findShop = shopService.findRandomShop(request, 2);
                    // then
                    assertThat(findShop)
                            .extracting("name", "price")
                            .containsExactly(
                                    "식당4", 3000
                            );
                })

        );
    }

    @DisplayName("검색 조건에 해당하는 식당이 없는 경우, 예외를 내보낸다.")
    @Test
    void findShopNoSearchResult(){
        // given
        Shop shop1 = createShop("식당1", 1000, RESTAURANT, NSC,
                LocalTime.of(17, 0), LocalTime.of(20, 0));
        Shop shop2 = createShop("식당2", 2000, RESTAURANT, NSC,
                LocalTime.of(17, 0), LocalTime.of(23, 0));
        shopRepository.saveAll(List.of(shop1, shop2));

        ShopSearchServiceRequest request = createFindShopRequest(List.of(RESTAURANT), NSC, LocalTime.of(1,0));

        // when // then
        assertThatThrownBy(()->shopService.findRandomShop(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조건에 맞는 식당이 존재하지 않습니다.");
    }

    private ShopSearchServiceRequest createFindShopRequest(List<ShopType> shopTypes, SchoolType schoolType, LocalTime time) {
        return ShopSearchServiceRequest.builder()
                .shopTypes(shopTypes)
                .schoolType(schoolType)
                .currentTime(time)
                .build();
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
}