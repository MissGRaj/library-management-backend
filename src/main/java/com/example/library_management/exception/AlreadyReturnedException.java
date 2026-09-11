package com.example.library_management.exception;

public class AlreadyReturnedException extends RuntimeException {

    public AlreadyReturnedException(String message) {
        super(message);
    }
}