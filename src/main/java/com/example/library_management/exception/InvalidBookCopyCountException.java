package com.example.library_management.exception;

public class InvalidBookCopyCountException extends RuntimeException {

    public InvalidBookCopyCountException(String message) {
        super(message);
    }
}