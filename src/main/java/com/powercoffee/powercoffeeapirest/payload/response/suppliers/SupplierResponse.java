package com.powercoffee.powercoffeeapirest.payload.response.suppliers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponse {
    private String id;
    private String nit;
    private String email;
    private String phoneNumber;
    private String name;
    private String coffeeShopId;
}
