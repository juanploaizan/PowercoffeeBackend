package com.powercoffee.powercoffeeapirest.payload.response.customers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerResponse {
    private String id;
    private String dni;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String coffeeShopId;
}
