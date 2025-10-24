package com.sedocefosse.backend.configs.exceptions;

public class InsufficientSupplyException extends RuntimeException {
    public InsufficientSupplyException(String message) {
        super(message);
    }
}
