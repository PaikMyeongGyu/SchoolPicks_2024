package org.schoolPicks.api.controller.shop;

import lombok.RequiredArgsConstructor;
import org.schoolPicks.api.controller.shop.request.ShopSearchRequest;
import org.schoolPicks.api.controller.shop.response.ShopSearchResponse;
import org.schoolPicks.api.service.shop.ShopService;
import org.schoolPicks.api.service.shop.request.ShopSearchServiceRequest;
import org.schoolPicks.domain.entity.shop.Shop;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/api/findShop")
    public ResponseEntity<ShopSearchResponse> findShop(@Valid @ModelAttribute ShopSearchRequest request){
        ShopSearchServiceRequest serviceRequest = request.toServiceRequest();

        Shop findShop = shopService.findRandomShop(serviceRequest);
        if(findShop == null){
            throw new IllegalArgumentException("조회된 값이 없습니다. 설정 값을 변경해주세요.");
        }

        ShopSearchResponse response = new ShopSearchResponse(findShop);
        return ResponseEntity.ok(response);
    }

}
