package com.powercoffee.powercoffeeapirest.payload.response.orders;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class OrderDetailResponse {
    private ProductOrderDetailResponse product;
    private Integer quantity;
    private Double productPrice;
    public Double subtotal;
}
