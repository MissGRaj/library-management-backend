package com.example.library_management.exception;

public class BookNotAvailableException extends RuntimeException{

    public BookNotAvailableException(String message) {
        super(message);
    }
}
