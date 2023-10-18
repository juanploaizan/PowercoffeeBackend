package com.powercoffee.service.Impl;

import com.powercoffee.model.User;
import com.powercoffee.payload.request.users.UserRequest;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.users.UserResponse;
import com.powercoffee.repository.UserRepository;
import com.powercoffee.security.jwt.JwtUtils;
import com.powercoffee.service.UserService;
import com.powercoffee.utils.services.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder encoder, JwtUtils jwtUtils, EmailService emailService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    @Override
    public PaginationResponse<UserResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponseList = userPage.getContent().stream().map(this::convertToResponse).toList();

        return new PaginationResponse<UserResponse>().build(
                userResponseList,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getNumberOfElements(),
                userPage.getTotalPages(),
                sortBy,
                sortDir,
                userPage.isFirst(),
                userPage.isLast()
        );
    }

    @Override
    public UserResponse getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest userRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        user.setUsername(userRequest.getUsername());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        return convertToResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        userRepository.delete(user);
    }

    @Override
    public void updatePassword(Integer id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        } else {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    @Override
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email " + email);
        }

        String token = jwtUtils.generateTokenFromUser(user);

        user.setResetPasswordToken(token);

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Recuperación de contraseña",
                "Hola " + user.getFirstName() + ", \n" +
                "Para recuperar tu contraseña, haz click en el siguiente enlace:\n"+
                        "http://localhost:8080/reset-password/"+token
        );
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        if (token.isBlank() || token.isEmpty()) {
            throw new IllegalArgumentException("Reset password error");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new EntityNotFoundException("User not found with username " + username);
        }
        if (!Objects.equals(user.getResetPasswordToken(), token)) {
            throw new IllegalArgumentException("Reset password error");
        }

        user.setPassword(encoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    private UserResponse convertToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

}
