package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.entity.Transaction;
import com.LmsTest.Lab5.repository.BookRepository;
import com.LmsTest.Lab5.repository.PatronRepository;
import com.LmsTest.Lab5.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllTransactions() {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.findAllTransactions();
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Size of result should match");
        assertTrue(result.contains(transaction1), "Result should contain transaction1");
        assertTrue(result.contains(transaction2), "Result should contain transaction2");
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testFindTransactionById() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = transactionService.findTransactionById(1L);
        assertTrue(result.isPresent(), "Transaction should be present");
        assertEquals(transaction, result.get(), "Transaction should match");
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testBorrowBookSuccess() {
        Book book = new Book(1L, "Title", "Author", "ISBN123456",new Date(), true);
        Patron patron = new Patron(1L, "First Name","Last name","name@gmail.com","false");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(bookRepository.save(book)).thenReturn(book);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.borrowBook(1L, 1L);

        verify(bookRepository, times(1)).save(book);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        assertFalse(book.isAvailability(), "Book should be marked as unavailable");
    }

    @Test
    void testBorrowBookPatronNotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transactionService.borrowBook(1L, 1L);
        });

        assertEquals("Patron not found", thrown.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testBorrowBookBookNotAvailable() {
        // Arrange
        // Create a Book object with availability set to false
        Book book = new Book(1L, "Title", "Author", "ISBN123456", new Date(), false); // Book not available
        Patron patron = new Patron(1L, "First Name", "Last Name", "name@gmail.com", "false");

        // Mock repository behavior
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        // Act & Assert
        // Expect a RuntimeException with the message "Book not available"
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transactionService.borrowBook(1L, 1L);
        });

        // Assert
        assertEquals("Book not available", thrown.getMessage());
        verify(bookRepository, never()).save(any(Book.class)); // Ensure save method is not called for book
        verify(transactionRepository, never()).save(any(Transaction.class)); // Ensure save method is not called for transaction
    }

    @Test
    void testReturnBookSuccess() {
        Book book = new Book(1L, "Title", "Author", "ISBN123456", new Date(),false);
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(bookRepository.save(book)).thenReturn(book);
        when(transactionRepository.save(transaction)).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.returnBook(1L);

        verify(bookRepository, times(1)).save(book);
        verify(transactionRepository, times(1)).save(transaction);
        assertTrue(book.isAvailability(), "Book should be marked as available");
        assertNotNull(transaction.getReturnDate(), "Return date should be set");
    }

    @Test
    void testReturnBookNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transactionService.returnBook(1L);
        });

        assertEquals("Transaction not found", thrown.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
