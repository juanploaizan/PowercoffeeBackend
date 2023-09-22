package com.powercoffee.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter @Setter
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String attributeName;
    private Integer attributeValue;

    public ResourceNotFoundException(String resourceName, String attributeName, Integer attributeValue) {
        super(String.format("%s no encontrado/a con: %s: '%s'", resourceName, attributeName, attributeValue));
        this.resourceName = resourceName;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
}
