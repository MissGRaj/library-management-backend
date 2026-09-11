package com.example.library_management.controller;

import com.example.library_management.dto.request.BookRequest;
import com.example.library_management.dto.request.BookSearchRequest;
import com.example.library_management.dto.response.BookResponse;
import com.example.library_management.entity.Book;
import com.example.library_management.exception.BookNotFoundException;
import com.example.library_management.service.BookService;
import com.example.library_management.service.CustomUserDetailsService;
import com.example.library_management.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldGetBookById() throws Exception{
//        Arrange
        BookResponse response = new BookResponse(
                1L,
                "Clean Code",
                "Robert Martin",
                3,
                3
        );

        when(bookService.getBookById(1L))
                .thenReturn(response);

//        Act + Assert
        mockMvc.perform(
                        get("/books/1")
                                .with(user("john").roles("USER"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin"));
    }

    @Test
    void shouldGetAllBooks() throws Exception{
//        Arrange
        BookResponse bookResponse1 = new BookResponse();
        bookResponse1.setId(1L);
        bookResponse1.setTitle("Clean Code");
        bookResponse1.setAuthor("Robert");

        BookResponse bookResponse2 = new BookResponse();
        bookResponse2.setId(2L);
        bookResponse2.setTitle("Effective Java");
        bookResponse2.setAuthor("Joshua Bloch");

        when(bookService.getBooks())
                .thenReturn(List.of(bookResponse1, bookResponse2));

//        Act + Assert
        mockMvc.perform(
                get("/books")
                        .with(user("john").roles("USER"))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Effective Java"));
    }

    @Test
    void shouldCreateBook() throws Exception{

        BookRequest request = new BookRequest();
        request.setTitle("Clean Code");
        request.setAuthor("Robert Martin");

        BookResponse response =
                new BookResponse(1L, "Clean Code", "Robert Martin", 3,3);

        when(bookService.addBook(any(BookRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/books")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Clean Code",
                                    "author": "Robert Martin"
                                }
                                """)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin"));
    }

    @Test
    void shouldUpdateBook() throws Exception {

        BookResponse response =
                new BookResponse(1L, "Effective Java", "Joshua Bloch", 3, 3);

        when(bookService.updateBook(
                eq(1L),
                any(BookRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/books/1")
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "Effective Java",
                                "author": "Joshua Bloch"
                            }
                            """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"));
    }

    @Test
    void shouldDeleteBook() throws Exception{

        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(
                delete("/books/1")
                        .with(user("admin").roles("ADMIN"))
        )
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    @Test
    void shouldSearchBooks() throws Exception {

        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(1L);
        bookResponse.setTitle("Clean Code");
        bookResponse.setAuthor("Robert Martin");

        Page<BookResponse> page = new PageImpl<>(
                List.of(bookResponse),
                PageRequest.of(0, 10),
                1
        );

        when(bookService.searchBooks(any(BookSearchRequest.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/books/search")
                                .param("title", "Clean Code")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "title")
                                .param("direction", "asc")
                                .with(user("john").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.content[0].author").value("Robert Martin"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldRejectInvalidBookRequest() throws Exception {

        mockMvc.perform(
                        post("/books")
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "",
                                "author": ""
                            }
                            """)
                )
                .andExpect(status().isBadRequest());

        verify(bookService, never())
                .addBook(any(BookRequest.class));
    }

    @Test
    void shouldReturn404WhenBookDoesNotExist() throws Exception {

        when(bookService.getBookById(999L))
                .thenThrow(new BookNotFoundException("Book Not Found with id 999"));

        mockMvc.perform(
                        get("/books/999")
                                .with(user("john").roles("USER"))
                )
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(999L);
    }

    @Test
    void shouldRejectInvalidPageSize() throws Exception {

        mockMvc.perform(
                        get("/books/search")
                                .param("size", "101")
                )
                .andExpect(status().isBadRequest());
    }


}
