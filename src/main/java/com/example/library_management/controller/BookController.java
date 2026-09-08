package com.example.library_management.controller;
import com.example.library_management.dto.request.BookRequest;
import com.example.library_management.dto.request.BookSearchRequest;
import com.example.library_management.dto.response.BookResponse;
import com.example.library_management.entity.Book;
import com.example.library_management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Books",
        description = "APIs for managing books in the Library Management System"
)

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }


//    Get All
    @Operation(
            summary = "Get all books",
            description = "Returns a list of all books available in the library."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/books")
    public List<BookResponse> getBooks(){

        return bookService.getBooks();
    }




//    Post
    @Operation(
            summary = "Create a new book",
            description = "Add a new book in the library."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Book added successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book data"
            )
    })
    @PostMapping("/books")
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(request));

    }



//    Get by ID
    @Operation(
            summary = "Get a book",
            description = "Return a book that matches the ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })
    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(
                    description = "Unique ID of the book",
                    example = "5"
            )
            @PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }



//    Update
    @Operation(
            summary = "Update an existing book",
            description = "Update an existing book in the library."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Book updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })

    @PutMapping("/books/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(
                    description = "Unique ID of the book",
                    example = "5"
            )
            @PathVariable Long id,
            @Valid @RequestBody BookRequest bookRequest){

        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
    }



// Delete
    @Operation(
            summary = "Delete a book",
            description = "Delete a book in the library."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Book deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(
                    description = "Unique ID of the book",
                    example = "5"
            )
            @PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }




// Search
    @Operation(
            summary = "Search books",
            description = "Returns a paginated list of books with optional sorting."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Books retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters"
            )
    })
    @GetMapping("/books/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @Valid @ModelAttribute BookSearchRequest request){

        return ResponseEntity.ok(bookService.searchBooks(request));
    }

//    jpql endpoints
//    @GetMapping("/books/search/jpql")
//    public ResponseEntity<List<Book>> searchBooksJPQL(@RequestParam String author){
//        return ResponseEntity.ok(bookService.searchBooksByAuthorJPQL(author));
//    }
//
//    @GetMapping("/books/search/jpql1")
//    public ResponseEntity<List<Book>> searchBooksJPQL1(@RequestParam String author,
//                                                       @RequestParam String title){
//        return ResponseEntity.ok(bookService.searchBooksJPQL(author, title));
//    }

//    pagination endpoint
//    @GetMapping("/books_page")
//    public ResponseEntity<Page<Book>> getBooks(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam String sortBy,
//            @RequestParam String direction){
//
//
//        return ResponseEntity.ok(bookService.searchBooks(pageable));
//    }

}
