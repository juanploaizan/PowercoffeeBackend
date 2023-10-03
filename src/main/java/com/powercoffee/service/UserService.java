package com.powercoffee.service;

import com.powercoffee.payload.request.users.UserRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.users.UserResponse;

public interface UserService {
    PaginationResponse<UserResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    UserResponse getUserById(Integer id);
    UserResponse updateUser(Integer id, UserRequest userRequest);
    void deleteUser(Integer id);
    void updatePassword(Integer id, String oldPassword, String newPassword);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
