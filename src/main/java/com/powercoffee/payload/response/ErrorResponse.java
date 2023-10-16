package com.powercoffee.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter @Setter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
