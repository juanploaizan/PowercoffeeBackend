package com.powercoffee.powercoffeeapirest.payload.response.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ProductOrderDetailResponse {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
}
