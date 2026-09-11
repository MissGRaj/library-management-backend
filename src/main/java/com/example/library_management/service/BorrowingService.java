package com.example.library_management.service;

import com.example.library_management.dto.response.BorrowingResponse;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.Borrowing;
import com.example.library_management.entity.BorrowingStatus;
import com.example.library_management.entity.User;
import com.example.library_management.exception.*;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.BorrowingRepository;
import com.example.library_management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowingService(
            BorrowingRepository borrowingRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {

        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BorrowingResponse borrowBook(Long bookId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found with username: " + username));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book Not Found with id " + bookId));

        boolean alreadyBorrowed =
                borrowingRepository.existsByUserAndBookAndStatus(
                        user,
                        book,
                        BorrowingStatus.BORROWED);

        if (alreadyBorrowed) {
            throw new BookAlreadyBorrowedException(
                    "You have already borrowed this book");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("The book '" + book.getTitle() + "' is not available for Borrowing");
        }

        Borrowing borrowing = new Borrowing();

        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowedAt(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(7));
        borrowing.setStatus(BorrowingStatus.BORROWED);

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        bookRepository.save(book);

        Borrowing savedBorrowing = borrowingRepository.save(borrowing);

        return new BorrowingResponse(
                savedBorrowing.getId(),
                savedBorrowing.getBook().getId(),
                savedBorrowing.getBook().getTitle(),
                savedBorrowing.getUser().getUsername(),
                savedBorrowing.getBorrowedAt(),
                savedBorrowing.getDueDate(),
                savedBorrowing.getReturnedAt(),
                savedBorrowing.getStatus(),
                false
        );
    }

    @Transactional
    public BorrowingResponse returnBook(Long borrowingId, String username) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() ->
                        new BorrowingNotFoundException(
                                "Borrowing Not Found with id " + borrowingId));

        if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
            throw new AlreadyReturnedException(
                    "Book has already been returned");
        }

        if (!borrowing.getUser().getUsername().equals(username)) {
            throw new BorrowingAccessDeniedException("You are not allowed to return this book");
        }

        borrowing.setReturnedAt(LocalDate.now());
        borrowing.setStatus(BorrowingStatus.RETURNED);

        Book book = borrowing.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);

        Borrowing savedBorrowing = borrowingRepository.save(borrowing);

        return new BorrowingResponse(
                savedBorrowing.getId(),
                savedBorrowing.getBook().getId(),
                savedBorrowing.getBook().getTitle(),
                savedBorrowing.getUser().getUsername(),
                savedBorrowing.getBorrowedAt(),
                savedBorrowing.getDueDate(),
                savedBorrowing.getReturnedAt(),
                savedBorrowing.getStatus(),
                false
        );
    }

    public List<BorrowingResponse> getMyBorrowings(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found with username: " + username));

        List<Borrowing> borrowings = borrowingRepository.findByUser(user);

        return borrowings.stream()
                .map(borrowing -> {

                    boolean overdue =
                            borrowing.getStatus() == BorrowingStatus.BORROWED
                                    && LocalDate.now().isAfter(borrowing.getDueDate());

                    return new BorrowingResponse(
                            borrowing.getId(),
                            borrowing.getBook().getId(),
                            borrowing.getBook().getTitle(),
                            borrowing.getUser().getUsername(),
                            borrowing.getBorrowedAt(),
                            borrowing.getDueDate(),
                            borrowing.getReturnedAt(),
                            borrowing.getStatus(),
                            overdue
                    );
                })
                .toList();



    }


}