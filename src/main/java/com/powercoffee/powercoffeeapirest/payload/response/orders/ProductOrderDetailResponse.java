package com.powercoffee.powercoffeeapirest.payload.response.orders;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductOrderDetailResponse {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
}
