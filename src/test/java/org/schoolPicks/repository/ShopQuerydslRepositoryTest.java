package org.schoolPicks.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.schoolPicks.entity.SchoolType;
import org.schoolPicks.entity.Shop;
import org.schoolPicks.entity.ShopType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ShopQuerydslRepositoryTest {

    @Autowired
    ShopQuerydslRepository shopQuerydslRepository;

    @Autowired
    ShopRepository shopRepository;

    @BeforeEach
    void initData(){

        Shop shop = Shop.builder()
                .name("식당1")
                .price(1000)
                .shopType(ShopType.RESTAURANT)
                .schoolType(SchoolType.NSC)
                .build();

        shopRepository.save(shop);

        Shop shop2 = Shop.builder()
                .name("식당2")
                .price(2000)
                .shopType(ShopType.CAFE)
                .schoolType(SchoolType.NSC)
                .build();

        shopRepository.save(shop2);

        Shop shop3 = Shop.builder()
                .name("식당3")
                .price(3000)
                .shopType(ShopType.PUB)
                .schoolType(SchoolType.NSC)
                .build();

        shopRepository.save(shop3);

        Shop shop4 = Shop.builder().name("식당4")
                .price(4000)
                .shopType(ShopType.RESTAURANT)
                .schoolType(SchoolType.HSSC)
                .build();

        shopRepository.save(shop4);

        Shop shop5 = Shop.builder()
                .name("식당5")
                .price(5000)
                .shopType(ShopType.CAFE)
                .schoolType(SchoolType.HSSC)
                .build();

        shopRepository.save(shop5);

        Shop shop6 = Shop.builder()
                .name("식당6")
                .price(6000)
                .shopType(ShopType.PUB)
                .schoolType(SchoolType.HSSC)
                .build();

        shopRepository.save(shop6);
    }

    @Test
    void 학교별_검색확인(){
        List<Shop> findNSCShop = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(SchoolType.NSC, null, null, null));
        assertThat(findNSCShop.size()).isEqualTo(3);

        List<Shop> findHSSCShop = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(SchoolType.HSSC, null, null, null));
        assertThat(findHSSCShop.size()).isEqualTo(3);
    }

    @Test
    void 데이터전체_검색확인(){
        List<Shop> findShops = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(null, null, null, null));
        assertThat(findShops.size()).isEqualTo(6);
    }

    @Test
    void 식당타입_개수별_검색확인(){
        List<Shop> findShops = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(null, List.of(ShopType.CAFE), null, null));
        assertThat(findShops.size()).isEqualTo(2);

        List<Shop> findShops2 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(null, List.of(ShopType.CAFE, ShopType.RESTAURANT), null, null));
        assertThat(findShops2.size()).isEqualTo(4);

        List<Shop> findShops3 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(null, List.of(ShopType.CAFE, ShopType.RESTAURANT, ShopType.PUB), null, null));
        assertThat(findShops3.size()).isEqualTo(6);
    }

    @Test
    void 식당가격별_검색확인(){
        List<Shop> findShops1 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(null, null, 1000, 3000));
        assertThat(findShops1.size()).isEqualTo(3);

        List<Shop> findShops2 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(null, null, 1000, 6000));
        assertThat(findShops2.size()).isEqualTo(6);

        List<Shop> findShops3 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(null, null, 10000, 20000));
        assertThat(findShops3.size()).isEqualTo(0);
    }

    @Test
    void 복합조건_검사(){
        List<Shop> findShops1 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(SchoolType.NSC, null, 1000, 2000));
        assertThat(findShops1.size()).isEqualTo(2);

        List<Shop> findShops2 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(SchoolType.NSC, List.of(ShopType.CAFE, ShopType.PUB), null, null));
        assertThat(findShops2.size()).isEqualTo(2);

        List<Shop> findShops3 = shopQuerydslRepository
                .findAll(new ShopQuerydslDto(SchoolType.NSC, List.of(ShopType.CAFE, ShopType.PUB, ShopType.RESTAURANT), 0, 1000));
        assertThat(findShops3.size()).isEqualTo(1);
    }
}