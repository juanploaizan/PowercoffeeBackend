package com.powercoffee.powercoffeeapirest.payload.request.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank(message = "Old password is required")
    @NotEmpty(message = "Old password must not be empty")
    @Size(min = 8, max = 25, message = "Old password must be between 8 and 20 characters")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @NotEmpty(message = "New password must not be empty")
    @Size(min = 8, max = 25, message = "Old password must be between 8 and 20 characters")
    private String newPassword;
}
