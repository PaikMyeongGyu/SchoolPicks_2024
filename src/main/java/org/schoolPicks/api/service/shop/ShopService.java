package org.schoolPicks.api.service.shop;

import lombok.RequiredArgsConstructor;
import org.schoolPicks.api.service.shop.request.ShopSearchServiceRequest;
import org.schoolPicks.domain.entity.shop.Shop;
import org.schoolPicks.domain.repository.shop.ShopQuerydslRepository;
import org.schoolPicks.domain.repository.shop.ShopRepository;
import org.schoolPicks.domain.repository.shop.condition.ShopSearchCond;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopService {

    private final ShopQuerydslRepository shopQuerydslRepository;
    Random random = new Random();

    public Shop findRandomShop(ShopSearchServiceRequest request, int offset){

        ShopSearchCond cond = createShopSearchCondWithoutTime(request);
        Shop findShop = shopQuerydslRepository.findShopBySearchCondAndOffset(cond, offset);

        if(findShop == null){
            throw new IllegalArgumentException("조건에 맞는 식당이 존재하지 않습니다.");
        }

        return findShop;
    }

    public Shop findRandomShop(ShopSearchServiceRequest request){

        ShopSearchCond cond = createShopSearchCondWithoutTime(request);
        Long shopCount = shopQuerydslRepository.findShopCount(cond);
        if(shopCount == 0){
            throw new IllegalArgumentException("조건에 맞는 식당이 존재하지 않습니다.");
        }

        int offset = random.nextInt(Math.toIntExact(shopCount));
        return findRandomShop(request, offset);
    }


    private ShopSearchCond createShopSearchCondWithoutTime(ShopSearchServiceRequest request) {
        return ShopSearchCond.builder()
                .shopTypes(request.getShopTypes())
                .schoolType(request.getSchoolType())
                .priceMin(request.getPriceMin())
                .priceMax(request.getPriceMax())
                .currentTime(request.getCurrentTime())
                .build();
    }
}
