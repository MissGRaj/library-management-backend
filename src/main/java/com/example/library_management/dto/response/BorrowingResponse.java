package com.example.library_management.dto.response;

import com.example.library_management.entity.BorrowingStatus;

import java.time.LocalDate;

public class BorrowingResponse {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String username;
    private LocalDate borrowedAt;
    private LocalDate dueDate;
    private LocalDate returnedAt;
    private BorrowingStatus status;
    private boolean overdue;

    public BorrowingResponse() {
    }

    public BorrowingResponse(
            Long id,
            Long bookId,
            String bookTitle,
            String username,
            LocalDate borrowedAt,
            LocalDate dueDate,
            LocalDate returnedAt,
            BorrowingStatus status,
            boolean overdue) {

        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.username = username;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
        this.status = status;
        this.overdue = overdue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDate borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDate returnedAt) {
        this.returnedAt = returnedAt;
    }

    public BorrowingStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowingStatus status) {
        this.status = status;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

}