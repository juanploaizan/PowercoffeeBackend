package com.powercoffee.exception;
import com.powercoffee.auth.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.*;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public AuthResponse handleBadCredentialsException(BadCredentialsException ex) {
        return AuthResponse.builder()
                .token(null)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public AuthResponse handleDisabledException(DisabledException ex) {
        return AuthResponse.builder()
                .token(null)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public AuthResponse handleLockedException(LockedException ex) {
        return AuthResponse.builder()
                .token(null)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public AuthResponse handleAuthenticationException(AuthenticationException ex) {
        return AuthResponse.builder()
                .token(null)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public AuthResponse handleRegistrationException(RegistrationException ex) {
        return AuthResponse.builder()
                .token(null)
                .message(ex.getMessage())
                .build();
    }

}
