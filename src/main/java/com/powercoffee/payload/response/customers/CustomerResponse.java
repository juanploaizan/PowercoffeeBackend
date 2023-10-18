package com.powercoffee.payload.response.customers;

import com.powercoffee.payload.response.coffeeshops.CoffeeShopResponse;
import com.powercoffee.payload.response.genders.GenderResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerResponse {
    private Integer id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private GenderResponse gender;
    private CoffeeShopResponse coffeeShop;
}
