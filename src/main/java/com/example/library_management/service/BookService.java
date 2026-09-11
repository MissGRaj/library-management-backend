package com.example.library_management.service;
import com.example.library_management.dto.request.BookRequest;
import com.example.library_management.dto.request.BookSearchRequest;
import com.example.library_management.dto.response.BookResponse;
import com.example.library_management.entity.Book;
import com.example.library_management.exception.BookNotFoundException;
import com.example.library_management.exception.InvalidSortFieldException;
import com.example.library_management.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookService {
    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "title", "author");

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){

        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getBooks(){

        return bookRepository.findAll()
                .stream()
                .map(book -> new BookResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getTotalCopies(),
                        book.getAvailableCopies()))
                .toList();
    }

    public BookResponse addBook(BookRequest request){

        Book book = new Book();
        book.setAuthor(request.getAuthor());
        book.setTitle(request.getTitle());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());

        Book savedBook = bookRepository.save(book);

        BookResponse bookResponse = new BookResponse();

        bookResponse.setId(savedBook.getId());
        bookResponse.setTitle(savedBook.getTitle());
        bookResponse.setAuthor(savedBook.getAuthor());
        bookResponse.setTotalCopies(savedBook.getTotalCopies());
        bookResponse.setAvailableCopies(savedBook.getAvailableCopies());

        return bookResponse;
    }

    public BookResponse getBookById(Long id){

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book Not Found with id " + id));
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }

    public BookResponse updateBook(Long id, BookRequest bookRequest){

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book Not Found with id " + id));
        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setAuthor(bookRequest.getAuthor());
        Book savedBook = bookRepository.save(existingBook);

        BookResponse bookResponse = new BookResponse();

        bookResponse.setId(savedBook.getId());
        bookResponse.setTitle(savedBook.getTitle());
        bookResponse.setAuthor(savedBook.getAuthor());
        bookResponse.setTotalCopies(savedBook.getTotalCopies());
        bookResponse.setAvailableCopies(savedBook.getAvailableCopies());

        return bookResponse;

    }

    public void deleteBook(Long id){

        Book book = bookRepository.findById(id)
                .orElseThrow(()
                -> new BookNotFoundException("Book Not Found With id " + id));
        bookRepository.deleteById(id);
    }


    public Page<BookResponse> searchBooks(BookSearchRequest request){

        if (!ALLOWED_SORT_FIELDS.contains(request.getSortBy())) {
            throw new InvalidSortFieldException(
                    "Invalid sort field: " + request.getSortBy()
            );
        }

        Sort sort = request.getDirection().equalsIgnoreCase("desc")
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        Page<Book> books;

        if(request.getAuthor() != null && request.getTitle() != null){
            books = bookRepository.findByAuthorAndTitleContainingIgnoreCase(
                            request.getAuthor(),
                            request.getTitle(),
                            pageable);
        }
        else if(request.getAuthor() != null) {
            books = bookRepository.findByAuthor(
                    request.getAuthor(),
                    pageable);
        }
        else if(request.getTitle() != null) {
            books = bookRepository.findByTitleContainingIgnoreCase(
                    request.getTitle(),
                    pageable);
        }
        else{
            books = bookRepository.findAll(pageable);
        }

        return books.map(book -> new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        ));
    }

//    jpql
//    public List<Book> searchBooksByAuthorJPQL(String author){
//        return bookRepository.findBooksByAuthorJPQL(author);
//    }
//
//    public List<Book> searchBooksJPQL(String author, String title){
//        return bookRepository.findBooksJPQL(author, title);
//    }

//    pagination
//    public Page<Book> searchBooks(Pageable pageable){
//        return bookRepository.findAll(pageable);
//    }
}
