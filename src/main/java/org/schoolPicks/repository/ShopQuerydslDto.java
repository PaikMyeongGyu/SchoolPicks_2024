package org.schoolPicks.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.schoolPicks.entity.SchoolType;
import org.schoolPicks.entity.ShopType;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class ShopQuerydslDto {

    public SchoolType schoolType;
    public List<ShopType> shopTypes;
    Integer priceMin;
    Integer priceMax;
}
