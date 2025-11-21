package com.sedocefosse.backend.configs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceInUseException extends RuntimeException {
    public ResourceInUseException(String message) {
        super(message);
    }
}
