package com.powercoffee.powercoffeeapirest.payload.response.orders;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerOrderResponse {
    private String id;
    private String dni;
    private String firstName;
    private String lastName;
}
