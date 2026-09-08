package com.example.library_management.service;

import com.example.library_management.dto.request.BookRequest;
import com.example.library_management.dto.request.BookSearchRequest;
import com.example.library_management.dto.response.BookResponse;
import com.example.library_management.entity.Book;
import com.example.library_management.exception.BookNotFoundException;
import com.example.library_management.exception.InvalidSortFieldException;
import com.example.library_management.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnBookById(){
//        Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

//        Act
        BookResponse response = bookService.getBookById(1L);

//        Assert
        assertEquals(1L, response.getId());
        assertEquals("Clean Code", response.getTitle());
        assertEquals("Robert Martin", response.getAuthor());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound(){

//        Arrange
        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

//        Act + Assert
        assertThrows(
                BookNotFoundException.class,
                () -> bookService.getBookById(999L)
        );
    }

    @Test
    void shouldAddBook(){

//        Arrange
        BookRequest request = new BookRequest();
        request.setTitle("Clean Code");
        request.setAuthor("Robert Martin");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Clean Code");
        savedBook.setAuthor("Robert Martin");

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);

//        Act
        BookResponse response = bookService.addBook(request);

//        Assert
        assertEquals(1L, response.getId());
        assertEquals("Clean Code", response.getTitle());
        assertEquals("Robert Martin", response.getAuthor());

    }

    @Test
    void shouldDeleteBook(){
//        Arrange
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

//        Act
        bookService.deleteBook(1L);

//        Assert
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingBook(){

//        Arrange
        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

//        Act + Assert
        assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(999L)
        );

//        Verify
        verify(bookRepository, never()).deleteById(999L);
    }

    @Test
    void shouldUpdateBook(){

//        Arrange
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");

        BookRequest request = new BookRequest();
        request.setTitle("New Title");
        request.setAuthor("New Author");

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(existingBook));

        when(bookRepository.save(existingBook))
                .thenReturn(existingBook);

//        Act
        BookResponse response = bookService.updateBook(1L, request);

//        Assert
        assertEquals(1L, response.getId());
        assertEquals("New Title", response.getTitle());
        assertEquals("New Author", response.getAuthor());

//        Verify
        verify(bookRepository).save(existingBook);
    }

    @Test
    void shouldThrowExceptionUpdatingNonExistingBook(){
//        Arrange
        BookRequest request = new BookRequest();
        request.setTitle("New Title");
        request.setAuthor("New Author");

        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

//        Act + Assert
        assertThrows(
                BookNotFoundException.class,
                () -> bookService.updateBook(999L, request)
        );

//        Verify
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldSearchBooksByAuthorAndTitle(){
//        Arrange
        BookSearchRequest request = new BookSearchRequest();
        request.setAuthor("Robert Martin");
        request.setTitle("Clean");
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("title");
        request.setDirection("asc");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");

        Page<Book> expectedPage = new PageImpl<>(
                List.of(book)
        );

        when(bookRepository.findByAuthorAndTitleContainingIgnoreCase(
                eq("Robert Martin"),
                eq("Clean"),
                any(Pageable.class)
        )).thenReturn(expectedPage);

//        Act
        Page<BookResponse> result = bookService.searchBooks(request);

//        Assert
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        assertEquals("Robert Martin", result.getContent().get(0).getAuthor());

//        Verify
        verify(bookRepository).findByAuthorAndTitleContainingIgnoreCase(
                eq("Robert Martin"),
                eq("Clean"),
                any(Pageable.class)
        );

    }

    @Test
    void shouldSearchBooksByAuthorOnly(){
        //        Arrange
        BookSearchRequest request = new BookSearchRequest();
        request.setAuthor("Robert Martin");
        request.setTitle(null);
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("title");
        request.setDirection("asc");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Robert Martin");

        Page<Book> expectedPage = new PageImpl<>(
                List.of(book)
        );

        when(bookRepository.findByAuthor(
                eq("Robert Martin"),
                any(Pageable.class)
        )).thenReturn(expectedPage);

//        Act
        Page<BookResponse> result = bookService.searchBooks(request);

//        Assert
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Title", result.getContent().get(0).getTitle());
        assertEquals("Robert Martin", result.getContent().get(0).getAuthor());

//        Verify
        verify(bookRepository).findByAuthor(
                eq("Robert Martin"),
                any(Pageable.class)
        );
    }

    @Test
    void shouldSearchBooksByTitleOnly(){
        //        Arrange
        BookSearchRequest request = new BookSearchRequest();
        request.setAuthor(null);
        request.setTitle("Clean");
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("title");
        request.setDirection("asc");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");

        Page<Book> expectedPage = new PageImpl<>(
                List.of(book)
        );

        when(bookRepository.findByTitleContainingIgnoreCase(
                eq("Clean"),
                any(Pageable.class)
        )).thenReturn(expectedPage);

//        Act
        Page<BookResponse> result = bookService.searchBooks(request);

//        Assert
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        assertEquals("Robert Martin", result.getContent().get(0).getAuthor());

//        Verify
        verify(bookRepository).findByTitleContainingIgnoreCase(
                eq("Clean"),
                any(Pageable.class)
        );
    }

    @Test
    void shouldReturnAllBooksWhenNoSearchFiltersProvided(){
        //        Arrange
        BookSearchRequest request = new BookSearchRequest();
        request.setAuthor(null);
        request.setTitle(null);
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("title");
        request.setDirection("asc");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");

        Page<Book> expectedPage = new PageImpl<>(
                List.of(book)
        );

        when(bookRepository.findAll(
                any(Pageable.class)
        )).thenReturn(expectedPage);

//        Act
        Page<BookResponse> result = bookService.searchBooks(request);

//        Assert
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        assertEquals("Robert Martin", result.getContent().get(0).getAuthor());

//        Verify
        verify(bookRepository).findAll(
                any(Pageable.class)
        );
    }

    @Test
    void shouldRejectInvalidSortField() {

        BookSearchRequest request = new BookSearchRequest();
        request.setSortBy("password");

        assertThrows(
                InvalidSortFieldException.class,
                () -> bookService.searchBooks(request)
        );
    }
}
