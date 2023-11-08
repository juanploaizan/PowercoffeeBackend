package com.powercoffee.powercoffeeapirest.payload.response.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Integer avatarNumber;
}
