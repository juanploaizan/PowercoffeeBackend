package com.powercoffee.powercoffeeapirest.service;

import com.powercoffee.powercoffeeapirest.payload.request.users.LoginGoogleRequest;
import com.powercoffee.powercoffeeapirest.payload.request.users.UserRequest;
import com.powercoffee.powercoffeeapirest.payload.response.users.UserJwtResponse;
import com.powercoffee.powercoffeeapirest.payload.response.users.UserResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;

public interface UserService {
    PaginationResponse<UserResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    UserResponse getUserById(Integer id);
    UserResponse updateUser(Integer id, UserRequest userRequest);
    void deleteUser(Integer id);
    void updatePassword(Integer id, String oldPassword, String newPassword);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword) throws Exception;
    void checkResetPasswordToken(String token);
    UserJwtResponse authenticateUserGoogle(LoginGoogleRequest loginGoogleRequest);
    UserJwtResponse getUserByEmail(String email);
}
