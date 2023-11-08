package com.powercoffee.powercoffeeapirest.payload.request.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank(message = "Category name cannot be blank")
    @NotEmpty(message = "Category name cannot be empty")
    @Size(min = 3, max = 60, message = "Category name must be between 3 and 60 characters")
    private String name;
}
