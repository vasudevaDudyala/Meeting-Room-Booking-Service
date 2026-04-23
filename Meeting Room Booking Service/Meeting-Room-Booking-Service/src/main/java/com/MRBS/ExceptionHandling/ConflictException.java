package com.MRBS.ExceptionHandling;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}