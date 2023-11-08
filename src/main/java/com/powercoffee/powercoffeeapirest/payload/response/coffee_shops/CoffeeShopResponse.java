package com.powercoffee.powercoffeeapirest.payload.response.coffee_shops;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CoffeeShopResponse {
    private String id;
    private String name;
    private String address;
    private String city;
    private Integer adminId;
}
