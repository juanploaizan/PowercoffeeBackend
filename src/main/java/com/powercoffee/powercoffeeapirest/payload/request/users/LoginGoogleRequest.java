package com.powercoffee.powercoffeeapirest.payload.request.users;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginGoogleRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
