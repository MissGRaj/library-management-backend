package com.example.library_management.repository;

import com.example.library_management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByAuthor(String author, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByAuthorAndTitleContainingIgnoreCase(String author, String title, Pageable pageable);

//    JPQL
//    @Query("""
//            SELECT b
//            FROM Book b
//            WHERE b.author = :author
//            """)
//    List<Book> findBooksByAuthorJPQL(@Param("author") String author);
//
//    @Query("""
//            SELECT b
//            FROM Book b
//            WHERE b.author = :author
//            AND LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))
//            """)
//    List<Book> findBooksJPQL(@Param("author") String author,
//                         @Param("title") String title);
}
