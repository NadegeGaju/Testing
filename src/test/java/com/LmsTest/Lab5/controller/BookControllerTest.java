package com.LmsTest.Lab5.controller;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "Title", "Author", "ISBN123456", new Date(), true);
    }

    @Test
    void testGetAllBooks() {
        when(bookService.findAllBooks()).thenReturn(Collections.singletonList(book));

        ResponseEntity<?> response = bookController.getAllBooks();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonList(book), response.getBody());
    }

    @Test
    void testGetBookById() {
        when(bookService.findBookById(anyLong())).thenReturn(Optional.of(book));

        ResponseEntity<?> response = bookController.getBookById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    void testCreateBook() {
        when(bookService.saveBook(any(Book.class))).thenReturn(book);

        ResponseEntity<?> response = bookController.createBook(book);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    void testUpdateBook() {
        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(Optional.of(book));

        ResponseEntity<?> response = bookController.updateBook(1L, book);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }


}
