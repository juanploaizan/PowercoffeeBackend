package com.powercoffee.powercoffeeapirest.payload.response.orders;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderResponse {
    private String id;
    private String date;
    private Double total;
    private String orderStatus;
    private CustomerOrderResponse customer;
    private EmployeeOrderResponse employee;
    private String coffeeShopId;
    private OrderDetailResponse[] orderDetails;
}
