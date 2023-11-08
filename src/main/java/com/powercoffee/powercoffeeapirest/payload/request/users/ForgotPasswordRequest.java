package com.powercoffee.powercoffeeapirest.payload.request.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {

    @Email(message = "Email must be valid")
    @Size(min = 6, max = 50, message = "Email must be between 6 and 50 characters")
    @NotEmpty(message = "Email must not be empty")
    @NotBlank(message = "Email must not be blank")
    private String email;
}
