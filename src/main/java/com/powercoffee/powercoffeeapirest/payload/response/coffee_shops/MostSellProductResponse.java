package com.powercoffee.powercoffeeapirest.payload.response.coffee_shops;

import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class MostSellProductResponse {
    private String name;
    private Long total;
}
