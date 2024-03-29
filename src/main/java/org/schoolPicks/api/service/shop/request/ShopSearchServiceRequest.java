package org.schoolPicks.api.service.shop.request;


import lombok.Builder;
import lombok.Data;
import org.schoolPicks.domain.entity.shop.SchoolType;
import org.schoolPicks.domain.entity.shop.ShopType;

import java.time.LocalTime;
import java.util.List;

@Data
public class ShopSearchServiceRequest {

    private SchoolType schoolType;
    private List<ShopType> shopTypes;
    private Integer priceMin;
    private Integer priceMax;
    private LocalTime currentTime;

    @Builder
    public ShopSearchServiceRequest(SchoolType schoolType, List<ShopType> shopTypes, Integer priceMin, Integer priceMax, LocalTime currentTime) {
        this.schoolType = schoolType;
        this.shopTypes = shopTypes;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.currentTime = currentTime;
    }
}
