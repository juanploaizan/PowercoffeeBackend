package com.powercoffee.powercoffeeapirest.payload.request.coffee_shops;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CoffeeShopRequest {

    @NotBlank(message = "Name cannot be blank")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
    private String name;

    @NotEmpty(message = "Address cannot be empty")
    @NotBlank(message = "Address cannot be blank")
    @Size(min = 3, max = 200, message = "Address must be between 3 and 200 characters")
    private String address;

    @Digits(integer = 10, fraction = 0, message = "Admin ID must be a number")
    private Integer adminId;

    @NotEmpty(message = "City cannot be empty")
    @NotBlank(message = "City cannot be blank")
    @Size(min = 3, max = 60, message = "City must be between 3 and 60 characters")
    private String city;
}
