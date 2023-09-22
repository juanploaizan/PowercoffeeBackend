package com.powercoffee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoffeeShopDTO {
    private Integer id;
    private String name;
    private String address;
}
