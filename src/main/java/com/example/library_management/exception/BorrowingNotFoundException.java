package com.example.library_management.exception;

public class BorrowingNotFoundException extends RuntimeException {

    public BorrowingNotFoundException(String message) {
        super(message);
    }
}