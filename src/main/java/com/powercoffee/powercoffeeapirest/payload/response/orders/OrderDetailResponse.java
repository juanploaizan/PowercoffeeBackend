package com.powercoffee.powercoffeeapirest.payload.response.orders;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDetailResponse {
    private ProductOrderDetailResponse product;
    private Integer quantity;
    private Double productPrice;
    public Double subtotal;
}
