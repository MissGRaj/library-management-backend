package com.example.library_management.repository;

import com.example.library_management.entity.Book;
import com.example.library_management.entity.Borrowing;
import com.example.library_management.entity.BorrowingStatus;
import com.example.library_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    boolean existsByUserAndBookAndStatus(
            User user,
            Book book,
            BorrowingStatus status);

    List<Borrowing> findByUser(User user);



}
