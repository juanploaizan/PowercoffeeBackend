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
    private Object attributeValue;

    public ResourceNotFoundException(String resourceName, String attributeName, Object attributeValue) {
        super(resourceName + " no encontrado/a con el " + attributeName + ": " + attributeValue);
        this.resourceName = resourceName;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
}
