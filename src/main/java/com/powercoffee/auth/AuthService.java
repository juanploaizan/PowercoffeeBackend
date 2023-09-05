package com.powercoffee.auth;

import com.powercoffee.enums.Role;
import com.powercoffee.exception.RegistrationException;
import com.powercoffee.jwt.JwtService;
import com.powercoffee.model.User;
import com.powercoffee.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserDetails user = userRepo.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtService.getToken(user);
            return AuthResponse.builder()
                    .token(token)
                    .message("Autenticación exitosa")
                    .build();
        } catch (DisabledException e) {
            throw new DisabledException("La cuenta está deshabilitada");
        } catch (LockedException e) {
            throw new LockedException("La cuenta está bloqueada");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales incorrectas");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Error de autenticación");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        if (validateData(request)) {
            throw new RegistrationException("Faltan atributos requeridos");
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RegistrationException("El correo electrónico ya está registrado");
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

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .message("Registro exitoso")
                .build();
    }

    private boolean validateData(RegisterRequest request) {
        return request.getEmail() == null || request.getPhoneNumber() == null || request.getFirstName() == null ||
                request.getLastName() == null || request.getPassword() == null ||
                request.getEmail().isEmpty() || request.getPhoneNumber().isEmpty() ||
                request.getFirstName().isEmpty() || request.getLastName().isEmpty() ||
                request.getPassword().isEmpty();
    }


}
