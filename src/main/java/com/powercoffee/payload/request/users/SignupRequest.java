package com.powercoffee.payload.request.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty")
    @Size(min = 10, max = 10, message = "Phone number must be 10 characters")
    private String phoneNumber;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 25, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 25 characters")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 25 characters")
    private String lastName;

    @NotEmpty(message = "Role cannot be empty")
    private Set<String> role;
}
