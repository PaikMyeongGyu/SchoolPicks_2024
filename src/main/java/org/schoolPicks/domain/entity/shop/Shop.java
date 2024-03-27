package org.schoolPicks.domain.entity.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@NoArgsConstructor
@Getter
public class Shop {

    @Id @GeneratedValue
    @Column(name = "SHOP_ID")
    private Long Id;

    private String name;

    private int price;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    private String url;

    private String description;
    private String menuDescription;

    private String xPosition;
    private String yPosition;

    private LocalTime openTime;
    private LocalTime closeTime;

    @Builder
    public Shop(String name, int price, ShopType shopType, SchoolType schoolType,
                String url, String description, String menuDescription,
                String xPosition, String yPosition,
                LocalTime openTime, LocalTime closeTime) {
        this.name = name;
        this.price = price;
        this.shopType = shopType;
        this.schoolType = schoolType;
        this.url = url;
        this.description = description;
        this.menuDescription = menuDescription;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
