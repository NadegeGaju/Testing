package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.repository.BookRepository;
import com.LmsTest.Lab5.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceExceptionTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    public TransactionServiceExceptionTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBookWhenPatronNotFound() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transactionService.borrowBook(1L, 1L);
        });

        assertEquals("Patron not found", thrown.getMessage());
    }

    @Test
    void testBorrowBookWhenBookNotAvailable() {
        Patron patron = new Patron(1L, "John", "Doe", "john@example.com", "password");
        Book book = new Book(1L, "Title", "Author", "ISBN123456", new Date(), false);  // Not available

        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transactionService.borrowBook(1L, 1L);
        });

        assertEquals("Book not available", thrown.getMessage());
    }
}
