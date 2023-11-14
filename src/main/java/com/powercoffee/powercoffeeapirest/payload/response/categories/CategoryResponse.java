package com.powercoffee.powercoffeeapirest.payload.response.categories;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CategoryResponse {
    private String id;
    private String name;
    private String coffeeShopId;
    private String createdAt;
}
