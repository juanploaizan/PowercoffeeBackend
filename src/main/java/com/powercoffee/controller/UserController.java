package com.powercoffee.controller;

import com.powercoffee.model.Role;
import com.powercoffee.model.User;
import com.powercoffee.model.enums.ERole;
import com.powercoffee.payload.request.users.*;
import com.powercoffee.payload.response.MessageResponse;
import com.powercoffee.payload.response.PaginationResponse;
import com.powercoffee.payload.response.users.UserJwtResponse;
import com.powercoffee.payload.response.users.UserResponse;
import com.powercoffee.repository.RoleRepository;
import com.powercoffee.repository.UserRepository;
import com.powercoffee.security.jwt.JwtUtils;
import com.powercoffee.security.services.UserDetailsImpl;
import com.powercoffee.service.UserService;
import com.powercoffee.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {

    final RoleRepository roleRepository;

    final UserRepository userRepository;

    final AuthenticationManager authenticationManager;

    final PasswordEncoder encoder;

    final JwtUtils jwtUtils;

    private final UserService userService;

    public UserController(RoleRepository roleRepository, UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtils jwtUtils, UserService userService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<UserResponse>> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                        @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwt(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserJwtResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already in use!"));
        }

        // Create new user's account
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .email(signUpRequest.getEmail())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "customer":
                        Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(customerRole);
                        break;

                    case "employee":
                        Role employeeRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(employeeRole);
                        break;

                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;

                    case "super_admin":
                        Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(superAdminRole);
                        break;
                    default:
                        throw new RuntimeException("Error: Role is not found.");
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest, @PathVariable Integer id) {
        return new ResponseEntity<>(userService.updateUser(id, userRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Integer id, @Valid @RequestBody ChangePasswordRequest request) {
        userService.updatePassword(id, request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/reset-password/{token}")
    public ResponseEntity<Void> resetPassword(@PathVariable String token, @Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(token, request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
