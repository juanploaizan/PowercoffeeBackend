package com.powercoffee.dto;

import com.powercoffee.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String role;
}
