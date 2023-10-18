package com.powercoffee.payload.response.coffeeshops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoffeeShopResponse {
    private Integer id;
    private String name;
    private String address;
}
