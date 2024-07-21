package com.LmsTest.Lab5.controller;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.entity.Transaction;
import com.LmsTest.Lab5.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private Transaction transaction;
    private Patron patron;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patron = new Patron(1L, "Aime","chris", "chris.aime@example.com","password");
        book = new Book(1L, "Sample Book", "Sample Author", "ISBN123456", new Date(), true);
        transaction = new Transaction(1L, patron, book, LocalDate.now(), LocalDate.now().plusDays(14), "borrow", null);
    }

    @Test
    void testGetAllTransactions() {
        when(transactionService.findAllTransactions()).thenReturn(Collections.singletonList(transaction));

        List<Transaction> response = transactionController.getAllTransactions();
        assertEquals(1, response.size());
        assertEquals(transaction, response.get(0));
    }

    @Test
    void testGetTransactionById() {
        when(transactionService.findTransactionById(anyLong())).thenReturn(Optional.of(transaction));

        ResponseEntity<Transaction> response = transactionController.getTransactionById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transaction, response.getBody());
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionService.findTransactionById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Transaction> response = transactionController.getTransactionById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testBorrowBook() {
        doNothing().when(transactionService).borrowBook(anyLong(), anyLong());

        ResponseEntity<Void> response = transactionController.borrowBook(1L, 1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testBorrowBook_Exception() {
        doThrow(new RuntimeException()).when(transactionService).borrowBook(anyLong(), anyLong());

        ResponseEntity<Void> response = transactionController.borrowBook(1L, 1L);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testReturnBook() {
        doNothing().when(transactionService).returnBook(anyLong());

        ResponseEntity<Void> response = transactionController.returnBook(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testReturnBook_Exception() {
        doThrow(new RuntimeException()).when(transactionService).returnBook(anyLong());

        ResponseEntity<Void> response = transactionController.returnBook(1L);
        assertEquals(400, response.getStatusCodeValue());
    }
}
