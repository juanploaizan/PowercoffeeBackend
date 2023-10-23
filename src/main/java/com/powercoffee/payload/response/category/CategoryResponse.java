package com.powercoffee.payload.response.category;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryResponse {
    private String id;
    private String name;
    private String coffeeShopId;
    private String createdAt;
}
