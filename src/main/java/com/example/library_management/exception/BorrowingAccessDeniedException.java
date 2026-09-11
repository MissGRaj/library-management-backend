package com.example.library_management.exception;

public class BorrowingAccessDeniedException extends RuntimeException {

    public BorrowingAccessDeniedException(String message) {
        super(message);
    }
}