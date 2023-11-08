package com.powercoffee.powercoffeeapirest.payload.request.orders;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class OrderDetailRequest {

    @NotEmpty(message = "Product ID is required")
    @NotBlank(message = "Product ID is required")
    @Size(min = 36, max = 36, message = "Product ID must be a valid UUID")
    private String productId;

    @Digits(integer = 3, fraction = 0, message = "Quantity must be a number")
    private Integer quantity;
}
