package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Transaction;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.repository.TransactionRepository;
import com.LmsTest.Lab5.repository.PatronRepository;
import com.LmsTest.Lab5.repository.BookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BookRepository bookRepository;

    private Patron patron;
    private Book book;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        patron = new Patron();
        patron.setFirstName("Aime");
        patron.setLastName("joh");
        patron.setEmail("joh@gmail.com");
        patron.setPassword("joh");
        patron = patronRepository.save(patron);

        book = new Book();
        book.setTitle("Spring Framework");
        book.setAuthor("Rod Johnson");
        book.setIsbn("1234567890");
        book = bookRepository.save(book);

        transaction = new Transaction();
        transaction.setPatron(patron);
        transaction.setBook(book);
        transaction.setIssueDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusDays(14));
        transaction.setType("borrow");
        transaction = transactionRepository.save(transaction);
    }

    @Test
    void testFindAllTransactions() {
        List<Transaction> transactions = transactionService.findAllTransactions();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).contains(transaction);
    }

    @Test
    void testFindTransactionById() {
        Optional<Transaction> foundTransaction = transactionService.findTransactionById(transaction.getId());
        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get()).isEqualTo(transaction);
    }
    @Test
    void testBorrowBook() {
        // Ensure the book is available before borrowing
        book.setAvailability(true); // Explicitly set book as available
        bookRepository.save(book);  // Save the book to ensure the state is persisted

        // Borrow the book
        transactionService.borrowBook(patron.getId(), book.getId());

        // Verify if the transaction has been created
        Optional<Transaction> transactionOptional = transactionRepository.findByPatronAndBookAndType(patron, book, "borrow");
        assertThat(transactionOptional).isPresent();
        Transaction borrowedTransaction = transactionOptional.get();
        assertThat(borrowedTransaction.getPatron()).isEqualTo(patron);
        assertThat(borrowedTransaction.getBook()).isEqualTo(book);
        assertThat(borrowedTransaction.getType()).isEqualTo("borrow");

        // Verify if the book status is updated
        Book updatedBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.isAvailability()).isFalse(); // The book should be marked as not available
    }

    @Test
    void testReturnBook() {
        // Create and save a transaction to be returned
        Transaction borrowTransaction = new Transaction();
        borrowTransaction.setPatron(patron);
        borrowTransaction.setBook(book);
        borrowTransaction.setIssueDate(LocalDate.now());
        borrowTransaction.setDueDate(LocalDate.now().plusWeeks(2));
        borrowTransaction.setType("borrow");
        transactionRepository.save(borrowTransaction);

        // Ensure the book is marked as not available
        book.setAvailability(false);
        bookRepository.save(book);

        // Simulate returning the book
        transactionService.returnBook(borrowTransaction.getId());

        // Fetch the updated transaction
        Optional<Transaction> transactionOptional = transactionRepository.findById(borrowTransaction.getId());
        assertThat(transactionOptional).isPresent();
        Transaction updatedTransaction = transactionOptional.get();

        // Assert that the transaction type has been updated to "return"
        assertThat(updatedTransaction.getType()).isEqualTo("return");
        assertThat(updatedTransaction.getReturnDate()).isNotNull();

        // Verify if the book status is updated
        Book updatedBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.isAvailability()).isTrue(); // The book should be marked as available
    }


}
