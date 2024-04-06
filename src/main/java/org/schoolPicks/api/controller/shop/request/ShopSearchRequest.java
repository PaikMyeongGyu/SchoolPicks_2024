package org.schoolPicks.api.controller.shop.request;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.schoolPicks.api.service.shop.request.ShopSearchServiceRequest;
import org.schoolPicks.domain.entity.shop.SchoolType;
import org.schoolPicks.domain.entity.shop.ShopType;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Data
public class ShopSearchRequest {

    @NotNull(message = "학교 설정은 필수입니다.")
    private SchoolType schoolType;

    private List<ShopType> shopTypes;

    @NotNull(message = "최소값 설정은 필수입니다.")
    @Range(min = 0, max = 20000, message = "최소값은 0-20000까지 설정이 가능합니다.")
    private Integer priceMin;

    @NotNull(message = "최대값 설정은 필수입니다.")
    @Range(min = 0, max = 20000, message = "최대값은 0-20000까지 설정이 가능합니다.")
    private Integer priceMax;

    @NotNull(message = "실시간 설정은 필수입니다.")
    private Boolean realTime;

    @AssertTrue( message = "최소값은 최대값을 넘을 수 없습니다.")
    public boolean isValidPrice(){
        if(priceMin != null && priceMax != null)
            return priceMin < priceMax;

        return false;
    }

    public ShopSearchServiceRequest toServiceRequest(){
        if(realTime){
            return ShopSearchServiceRequest.builder()
                    .schoolType(schoolType)
                    .shopTypes(shopTypes)
                    .currentTime(LocalTime.now())
                    .priceMin(priceMin)
                    .priceMax(priceMax)
                    .build();
        }
        return ShopSearchServiceRequest.builder()
                .schoolType(schoolType)
                .shopTypes(shopTypes)
                .priceMin(priceMin)
                .priceMax(priceMax)
                .build();
    }

    @Builder
    public ShopSearchRequest(SchoolType schoolType, List<ShopType> shopTypes, Integer priceMin, Integer priceMax, Boolean realTime) {
        this.schoolType = schoolType;
        this.shopTypes = shopTypes;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.realTime = realTime;
    }
}
