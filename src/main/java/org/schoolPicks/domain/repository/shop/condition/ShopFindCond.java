package org.schoolPicks.domain.repository.shop.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.schoolPicks.domain.entity.shop.SchoolType;
import org.schoolPicks.domain.entity.shop.ShopType;

import java.time.LocalTime;
import java.util.List;

@Data
public class ShopFindCond {

    private SchoolType schoolType;
    private List<ShopType> shopTypes;
    private Integer priceMin;
    private Integer priceMax;
    private LocalTime currentTime;

    @Builder
    public ShopFindCond(SchoolType schoolType, List<ShopType> shopTypes, Integer priceMin, Integer priceMax, LocalTime currentTime) {
        this.schoolType = schoolType;
        this.shopTypes = shopTypes;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.currentTime = currentTime;
    }
}
