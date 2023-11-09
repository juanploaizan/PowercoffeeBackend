package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.User;
import com.powercoffee.powercoffeeapirest.payload.request.users.UserRequest;
import com.powercoffee.powercoffeeapirest.payload.response.users.UserResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.repository.UserRepository;
import com.powercoffee.powercoffeeapirest.security.jwt.JwtUtils;
import com.powercoffee.powercoffeeapirest.service.UserService;
import com.powercoffee.powercoffeeapirest.utils.services.EmailService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${powercoffee.app.frontendUrl}")
    private String frontendUrl;

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

        if (!Objects.equals(user.getEmail(), userRequest.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new EntityExistsException("Email is already in use");
            }
        }

        if (!Objects.equals(user.getPhoneNumber(), userRequest.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
                throw new EntityExistsException("Phone number is already in use");
            }
        }

        if (!Objects.equals(user.getUsername(), userRequest.getUsername())) {
            if (userRepository.existsByUsername(userRequest.getUsername())) {
                throw new EntityExistsException("Username is already taken");
            }
        }

        user.setUsername(userRequest.getUsername());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setAvatarNumber(userRequest.getAvatarNumber());

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
                "Restablecimiento de Contraseña",
                "Estimado/a " + user.getFirstName() + ", \n\n" +
                        "Hemos recibido una solicitud para restablecer la contraseña de tu cuenta. " +
                        "Para continuar con este proceso, haz clic en el siguiente enlace o cópialo y pégalo en la barra de direcciones de tu navegador: \n" +
                        frontendUrl + "/reset-password/" + token + "\n\n" +
                        "Si no solicitaste restablecer tu contraseña, ignora este mensaje y tu contraseña permanecerá sin cambios.\n\n" +
                        "Si tienes alguna pregunta o necesitas ayuda adicional, no dudes en contactar a nuestro equipo de soporte en servicioalciente.powercoffee@gmail.com y estaremos encantados de ayudarte.\n\n" +
                        "Atentamente,\n" +
                        "El equipo de PowerCoffee."
        );
    }

    @Override
    public void resetPassword(String token, String newPassword) throws Exception {

        if (token.isBlank() || token.isEmpty()) {
            throw new Exception("Token is required");
        }

        String email = jwtUtils.getUserNameFromJwtToken(token);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new EntityNotFoundException("User not found with email " + email);
        }
        if (!Objects.equals(user.getResetPasswordToken(), token)) {
            throw new Exception("Invalid token");
        }

        user.setPassword(encoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Override
    public void checkResetPasswordToken(String token) {
        if (token.isBlank() || token.isEmpty()) {
            throw new IllegalArgumentException("Token is required");
        }

        String email = jwtUtils.getUserNameFromJwtToken(token);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new EntityNotFoundException("User not found with email " + email);
        }
        if (!Objects.equals(user.getResetPasswordToken(), token)) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private UserResponse convertToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

}
