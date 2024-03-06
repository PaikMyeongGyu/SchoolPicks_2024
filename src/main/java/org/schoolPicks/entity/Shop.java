package org.schoolPicks.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Builder
@AllArgsConstructor
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

}
