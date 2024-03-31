package org.schoolPicks.api.controller.shop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.schoolPicks.api.controller.shop.request.ShopSearchRequest;
import org.schoolPicks.api.service.shop.ShopService;
import org.schoolPicks.domain.entity.shop.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
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
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("학교 설정은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("식당을 검색할 때에 최소값은 필수이다.")
    @Test
    void searchShopWithoutPriceMin() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMax=20000&realTime=TRUE")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("최소값 설정은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("식당을 검색할 때에 최소값의 범위는 0-20000사이의 값이다.")
    @Test
    void searchShopWithoutPriceMinRange() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=25000&priceMax=20000&realTime=TRUE")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("최소값은 0-20000까지 설정이 가능합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("식당을 검색할 때에 최대값은 필수이다.")
    @Test
    void searchShopWithoutPriceMax() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=10000&realTime=TRUE")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("최대값 설정은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("식당을 검색할 때에 최대값의 범위는 0-20000사이의 값이다.")
    @Test
    void searchShopWithoutPriceMaxRange() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=10000&priceMax=25000&realTime=TRUE")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("최대값은 0-20000까지 설정이 가능합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("식당을 검색할 때에 실시간 설정은 필수이다.")
    @Test
    void searchShopWithoutRealTime() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=10000&priceMax=15000")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("실시간 설정은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("식당을 검색할 때에 최소값은 최대값을 넘을 수 없다.")
    @Test
    void searchShopMinCantHigherThanMax() throws Exception {
        // given // when // then
        mockMvc.perform(get("/api/findShop?schoolType=NSC&priceMin=20000&priceMax=10000&realTime=TRUE")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("실시간 설정은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());


    }
}