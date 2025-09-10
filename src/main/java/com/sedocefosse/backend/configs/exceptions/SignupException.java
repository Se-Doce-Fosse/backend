package com.sedocefosse.backend.configs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="User already activated")
public class SignupException extends RuntimeException {
    public SignupException(String message) {
        super(message);
    }
}
