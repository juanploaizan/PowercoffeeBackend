package com.powercoffee.powercoffeeapirest.payload.response.coffee_shops;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CoffeeShopResponse {
    private String id;
    private String name;
    private String address;
    private String city;
    private Integer adminId;
}
