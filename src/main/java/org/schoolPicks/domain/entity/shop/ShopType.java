package org.schoolPicks.domain.entity.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShopType {
    RESTAURANT("식당"),
    CAFE("카페"),
    PUB("술집");

    private final String text;
}
