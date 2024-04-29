package org.schoolPicks.docs.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.schoolPicks.api.controller.shop.ShopController;
import org.schoolPicks.api.service.shop.ShopService;
import org.schoolPicks.api.service.shop.request.ShopSearchServiceRequest;
import org.schoolPicks.docs.RestDocsSupport;
import org.schoolPicks.domain.entity.shop.Shop;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.schoolPicks.domain.entity.shop.SchoolType.NSC;
import static org.schoolPicks.domain.entity.shop.ShopType.RESTAURANT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ShopControllerDocsTest extends RestDocsSupport {

    private final ShopService shopService = mock(ShopService.class);

    @Override
    protected Object initController() {
        return new ShopController(shopService);
    }

    @DisplayName("검색된 식당을 반환하는 API")
    @Test
    void shopSearch() throws Exception {
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
                .andExpect(jsonPath("$.price").value(8000))
                .andDo(document("shop-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("schoolType").description("학교 타입(NSC, HSC)"),
                                parameterWithName("shopTypes").optional().description("식당 타입 리스트(RESTAURANT, CAFE, PUB)"),
                                parameterWithName("priceMin").description("하한 가격"),
                                parameterWithName("priceMax").description("상한 가격"),
                                parameterWithName("realTime").description("실시간 조회")
                        ),
                        responseFields(
                                fieldWithPath("shopName").type(JsonFieldType.STRING)
                                        .description("가게 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("가격")
                        )));
    }
}
