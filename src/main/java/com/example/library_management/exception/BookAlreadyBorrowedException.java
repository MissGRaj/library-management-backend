package com.example.library_management.exception;

public class BookAlreadyBorrowedException extends RuntimeException {

    public BookAlreadyBorrowedException(String message) {
        super(message);
    }
}
