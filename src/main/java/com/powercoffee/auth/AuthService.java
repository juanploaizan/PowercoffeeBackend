package com.powercoffee.auth;

import com.powercoffee.enums.Role;
import com.powercoffee.exception.AuthException;
import com.powercoffee.jwt.JwtService;
import com.powercoffee.model.User;
import com.powercoffee.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.util.Locale;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo; // Repositorio para obtener el usuario por su email
    private final JwtService jwtService; // Servicios relacionados con el JWT
    private final PasswordEncoder passwordEncoder; // Encriptador de la contraseña
    private final AuthenticationManager authenticationManager; // Permite autenticarse dentro del server
    private final MessageSource messageSource;

    public String generateJwt(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            throw new BadCredentialsException(messageSource.getMessage("error_INVALID_CREDENTIALS", null, Locale.getDefault()));
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            return jwtService.getToken(user);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(null);
        } catch (Exception e) {
            throw new RuntimeException(messageSource.getMessage("error_AUTH_ERROR", null, Locale.getDefault()));
        }
    }

    public String register(RegisterRequest request) throws AuthException {
        if (isInvalidateData(request)) {
            throw new AuthException(messageSource.getMessage("error_INVALID_DATA", null, Locale.getDefault()));
        }

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthException(messageSource.getMessage("error_EMAIL_ALREADY_USED", null, Locale.getDefault()));
        }

        if (userRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new AuthException(messageSource.getMessage("error_PHONE_NUMBER_ALREADY_USED", null, Locale.getDefault()));
        }

        User user = User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepo.save(user);

        return jwtService.getToken(user);
    }

    private boolean isInvalidateData(RegisterRequest request) {
        // TODO revisar, además, la estructura de los datos (longitud, regex, etc)
        return Stream.of(
                request.getEmail(),
                request.getPhoneNumber(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword())
            .anyMatch(field -> field == null || field.isEmpty());
    }


}
