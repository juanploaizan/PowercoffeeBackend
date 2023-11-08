package com.powercoffee.powercoffeeapirest.payload.request.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequest {
    @NotBlank(message = "Order status is required")
    @NotEmpty(message = "Order status is required")
    private String orderStatus;

    @NotBlank(message = "Customer ID is required")
    @NotEmpty(message = "Customer ID is required")
    @Size(min = 36, max = 36, message = "Category ID must be a valid UUID")
    private String customerId;

    @NotBlank(message = "Employee ID is required")
    @NotEmpty(message = "Employee ID is required")
    @Size(min = 36, max = 36, message = "Category ID must be a valid UUID")
    private String employeeId;

    @NotEmpty(message = "Order Details is required")
    @Valid
    private OrderDetailRequest[] orderDetails;
}
