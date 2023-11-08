package com.powercoffee.powercoffeeapirest.payload.request.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordRequest {

    @NotEmpty(message = "New password must not be empty")
    @NotBlank(message = "New password must not be blank")
    @Size(min = 8, max = 60, message = "")
    private String newPassword;
}
