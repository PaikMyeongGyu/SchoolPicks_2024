package org.schoolPicks.api.controller.shop;

import lombok.RequiredArgsConstructor;
import org.schoolPicks.api.controller.ApiResponse;
import org.schoolPicks.api.controller.shop.request.ShopSearchRequest;
import org.schoolPicks.api.controller.shop.response.ShopSearchResponse;
import org.schoolPicks.api.service.shop.ShopService;
import org.schoolPicks.api.service.shop.request.ShopSearchServiceRequest;
import org.schoolPicks.domain.entity.shop.Shop;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/api/findShop")
    public ApiResponse<ShopSearchResponse> findShop(@Valid @ModelAttribute ShopSearchRequest request){
        if(request.getPriceMin() > request.getPriceMax()){
            throw new IllegalArgumentException("최소값은 최대값보다 클 수 없습니다.");
        }

        ShopSearchServiceRequest serviceRequest = request.toServiceRequest();
        Shop findShop = shopService.findRandomShop(serviceRequest);
        if(findShop == null){
            throw new IllegalArgumentException("조회된 값이 없습니다. 설정 값을 변경해주세요.");
        }
        ShopSearchResponse response = new ShopSearchResponse(findShop);
        return ApiResponse.ok(response);
    }
}
