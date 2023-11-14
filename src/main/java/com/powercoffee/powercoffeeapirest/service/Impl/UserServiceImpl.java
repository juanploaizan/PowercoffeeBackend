package com.powercoffee.powercoffeeapirest.service.Impl;

import com.powercoffee.powercoffeeapirest.model.Role;
import com.powercoffee.powercoffeeapirest.model.User;
import com.powercoffee.powercoffeeapirest.model.enums.ERole;
import com.powercoffee.powercoffeeapirest.payload.request.users.LoginGoogleRequest;
import com.powercoffee.powercoffeeapirest.payload.request.users.UserRequest;
import com.powercoffee.powercoffeeapirest.payload.response.users.UserJwtResponse;
import com.powercoffee.powercoffeeapirest.payload.response.users.UserResponse;
import com.powercoffee.powercoffeeapirest.payload.response.utils.PaginationResponse;
import com.powercoffee.powercoffeeapirest.repository.RoleRepository;
import com.powercoffee.powercoffeeapirest.repository.UserRepository;
import com.powercoffee.powercoffeeapirest.security.jwt.JwtUtils;
import com.powercoffee.powercoffeeapirest.security.services.UserDetailsImpl;
import com.powercoffee.powercoffeeapirest.service.LoggerService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final LoggerService loggerService;

    private final EmailService emailService;

    @Value("${powercoffee.app.frontendUrl}")
    private String frontendUrl;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder encoder, JwtUtils jwtUtils, LoggerService loggerService, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.loggerService = loggerService;
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        UserResponse userResponse = convertToResponse(user);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("User", "READ", userResponse, userResponse, userId);

        return userResponse;
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest userRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        UserResponse previousUserResponse = convertToResponse(user);

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

        UserResponse actualUserResponse = convertToResponse(user);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("User", "UPDATE", previousUserResponse, actualUserResponse, userId);

        return convertToResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        UserResponse previousUserResponse = convertToResponse(user);
        Integer userId = obtainIdFromJwtToken();
        loggerService.logAction("User", "DELETE", previousUserResponse, null, userId);
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

    @Override
    public UserJwtResponse authenticateUserGoogle(LoginGoogleRequest loginGoogleRequest) {
        // Obtener el usuario o crearlo si no existe
        User user = userRepository.findByEmail(loginGoogleRequest.getEmail()).orElseGet(() -> {
            User newUser = User.builder()
                    .username(getUsernameFromEmail(loginGoogleRequest.getEmail()))
                    .email(loginGoogleRequest.getEmail())
                    .firstName(loginGoogleRequest.getFirstName())
                    .lastName(loginGoogleRequest.getLastName())
                    .password(encoder.encode(loginGoogleRequest.getPassword()))
                    .build();
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            newUser.setRoles(roles);
            return userRepository.save(newUser);
        });

        // Generar el token JWT
        String jwt = jwtUtils.generateTokenFromUser(user);

        // Retornar la respuesta
        return new UserJwtResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                jwt
        );
    }

    @Override
    public UserJwtResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String jwt = jwtUtils.generateTokenFromUser(user);
            return new UserJwtResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    null,
                    jwt
            );
        }
        return null;
    }


    private String getUsernameFromEmail(String email) {
        return email.split("@")[0];
    }

    private UserResponse convertToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    private Integer obtainIdFromJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
            return user.getId();
        }

        return null;
    }
}
