package com.powercoffee.service;

import com.powercoffee.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<User> getAllUsers(Pageable pageable);

    User getUserById(Integer id);

    User updateUser(User user);

    void deleteUser(Integer id);

}
