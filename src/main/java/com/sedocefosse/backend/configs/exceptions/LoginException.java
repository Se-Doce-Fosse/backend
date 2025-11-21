package com.sedocefosse.backend.configs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Invalid password or username")
public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}
