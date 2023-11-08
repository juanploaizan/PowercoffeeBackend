package com.powercoffee.powercoffeeapirest.payload.response.coffee_shops;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecentOrderResponse {
    private String clientName;
    private String clientEmail;
    private String initials;
    private Double total;
}
