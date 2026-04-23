package com.MRBS.ExceptionHandling;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}