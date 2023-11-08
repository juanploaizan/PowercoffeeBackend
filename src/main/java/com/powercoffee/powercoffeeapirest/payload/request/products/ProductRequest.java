package com.powercoffee.powercoffeeapirest.payload.request.products;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductRequest {
    @NotEmpty(message = "Name must not be empty")
    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    @NotBlank(message = "Description must not be blank")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters")
    private String description;

    @NotEmpty(message = "Image URL must not be empty")
    @NotBlank(message = "Image URL must not be blank")
    @Size(min = 3, max = 255, message = "Image URL must be between 3 and 255 characters")
    private String imageUrl;

    @Digits(integer = 10, fraction = 2, message = "Purchase price must be a number with maximum 2 decimal places")
    private Double purchasePrice;

    @Digits(integer = 10, fraction = 2, message = "Sale price must be a number with maximum 2 decimal places")
    private Double salePrice;

    @Digits(integer = 10, fraction = 0, message = "Stock must be a number with maximum 0 decimal places")
    private Integer stock;

    @NotEmpty(message = "Category ID must not be empty")
    @NotBlank(message = "Category ID must not be blank")
    @Size(min = 36, max = 36, message = "Category ID must be a valid UUID")
    private String categoryId;
}
