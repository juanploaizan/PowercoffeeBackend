package com.powercoffee.auth;

import com.powercoffee.exception.AuthException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    @Data
    @AllArgsConstructor
    public static class JwtResponse {
        private String token;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            String jwt = authService.generateJwt(request);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(messageSource.getMessage("error_INVALID_CREDENTIALS", null, Locale.getDefault()));
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        try {
            String jwt = authService.register(request);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch(AuthException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("error_INTERNAL_SERVER_ERROR", null, Locale.getDefault()));
        }
    }
}
