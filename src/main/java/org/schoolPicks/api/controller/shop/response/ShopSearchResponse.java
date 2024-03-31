package org.schoolPicks.api.controller.shop.response;

import lombok.Getter;
import org.schoolPicks.api.controller.shop.request.ShopSearchRequest;
import org.schoolPicks.domain.entity.shop.Shop;

@Getter
public class ShopSearchResponse {

    String shopName;
    int price;

    public ShopSearchResponse(Shop shop){
        this.shopName = shop.getName();
        this.price = shop.getPrice();
    }
}
