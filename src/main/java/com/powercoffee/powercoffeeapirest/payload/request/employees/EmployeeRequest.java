package com.powercoffee.powercoffeeapirest.payload.request.employees;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeRequest {
    @NotEmpty(message = "Document number cannot be empty")
    @NotBlank(message = "Document number cannot be blank")
    @Size(min = 6, max = 10, message = "DNI must be between 6 and 10 characters")
    private String dni;

    @Email(message = "Email must be valid")
    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "Email cannot be blank")
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty")
    @NotBlank(message = "Phone number cannot be blank")
    @Size(min = 10, max = 10, message = "Phone number must be 10 characters")
    private String phoneNumber;

    @NotEmpty(message = "First name cannot be empty")
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    private String lastName;

    @NotEmpty(message = "Birthdate cannot be empty")
    @NotBlank(message = "Birthdate cannot be blank")
    @Size(min = 10, max = 10, message = "Birthdate must be 10 characters (yyyy-mm-dd)")
    private String birthdate;

    @Digits(integer = 10, fraction = 2, message = "Purchase price must be a number with maximum 2 decimal places")
    private Double salary;

    @NotEmpty(message = "Address cannot be empty")
    @NotBlank(message = "Address cannot be blank")
    @Size(min = 2, max = 70, message = "Address must be between 2 and 70 characters")
    private String address;

    @NotEmpty(message = "Hire Date cannot be empty")
    @NotBlank(message = "Hire Date cannot be blank")
    @Size(min = 10, max = 10, message = "Birthdate must be 10 characters (yyyy-mm-dd)")
    private String hireDate;

    @NotEmpty(message = "Gender cannot be empty")
    @NotBlank(message = "Gender cannot be blank")
    @Size(min = 1, max = 1, message = "Gender must be 1 character (M or F)")
    private String gender;
}
