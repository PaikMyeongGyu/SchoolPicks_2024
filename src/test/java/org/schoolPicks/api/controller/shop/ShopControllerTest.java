package org.schoolPicks.api.controller.shop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.schoolPicks.api.controller.shop.request.ShopSearchRequest;
import org.schoolPicks.api.service.shop.ShopService;
import org.schoolPicks.api.service.shop.request.ShopSearchServiceRequest;
import org.schoolPicks.domain.entity.shop.SchoolType;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.entity.shop.ShopType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.schoolPicks.domain.entity.shop.SchoolType.*;
import static org.schoolPicks.domain.entity.shop.ShopType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShopController.class)
class ShopControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ShopService shopService;

    @DisplayName("식당을 검색할 때에 학교 값은 필수이다.")
    @Test
    void searchShopWithoutSchoolType() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?priceMin=0&priceMax=20000&realTime=TRUE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("학교 설정은 필수입니다."));
    }

    @DisplayName("식당을 검색할 때에 최소값은 필수이다.")
    @Test
    void searchShopWithoutPriceMin() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMax=20000&realTime=TRUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("최소값 설정은 필수입니다."));
    }

    @DisplayName("식당을 검색할 때에 최소값의 범위는 0-20000사이의 값이다.")
    @Test
    void searchShopWithoutPriceMinRange() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=-1&priceMax=10000&realTime=TRUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("최소값은 0-20000까지 설정이 가능합니다."));
    }

    @DisplayName("식당을 검색할 때에 최대값은 필수이다.")
    @Test
    void searchShopWithoutPriceMax() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=10000&realTime=TRUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("최대값 설정은 필수입니다."));
    }

    @DisplayName("식당을 검색할 때에 최대값의 범위는 0-20000사이의 값이다.")
    @Test
    void searchShopWithoutPriceMaxRange() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=10000&priceMax=25000&realTime=TRUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("최대값은 0-20000까지 설정이 가능합니다."));
    }

    @DisplayName("식당을 검색할 때에 실시간 설정은 필수이다.")
    @Test
    void searchShopWithoutRealTime() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=10000&priceMax=15000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("실시간 설정은 필수입니다."));
    }

    @DisplayName("식당을 검색할 때에 최소값은 최대값을 넘을 수 없다.")
    @Test
    void searchShopMinCantHigherThanMax() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=20000&priceMax=10000&realTime=TRUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("최소값은 최대값을 넘을 수 없습니다."));
    }

    @DisplayName("검색된 식당이 있는 경우 식당을 반환한다.")
    @Test
    void shopSearchSuccess() throws Exception {
        // given
        Shop shop = Shop.builder()
                .name("학식")
                .shopType(RESTAURANT)
                .schoolType(NSC)
                .price(8000)
                .openTime(LocalTime.MIN)
                .closeTime(LocalTime.MAX)
                .build();

        ShopSearchServiceRequest serviceRequest =
                new ShopSearchServiceRequest(NSC, List.of(RESTAURANT), 5000, 10000, null);

        given(shopService.findRandomShop(serviceRequest))
                .willReturn(shop);

        // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&shopTypes=RESTAURANT&priceMin=5000&priceMax=10000&realTime=FALSE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shopName").value("학식"))
                .andExpect(jsonPath("$.price").value(8000));
    }

    @DisplayName("검색 조건의 식당이 없는 경우, 예외를 던진다.")
    @Test
    void shopSearchFail() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=5000&priceMax=10000&realTime=TRUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("조회된 값이 없습니다. 설정 값을 변경해주세요."));
    }


}