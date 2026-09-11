package com.example.library_management.dto.request;

import jakarta.validation.constraints.NotNull;

public class BorrowingRequest {

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    public BorrowingRequest() {
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}