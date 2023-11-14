package com.powercoffee.powercoffeeapirest.payload.response.employees;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class EmployeeResponse {
    private String id;
    private String dni;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String birthdate;
    private Double salary;
    private String address;
    private String hireDate;
    private String gender;
    private String coffeeShopId;
}
