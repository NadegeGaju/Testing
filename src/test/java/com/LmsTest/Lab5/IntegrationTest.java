package com.LmsTest.Lab5;


import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.entity.Transaction;
import com.LmsTest.Lab5.repository.BookRepository;
import com.LmsTest.Lab5.repository.PatronRepository;
import com.LmsTest.Lab5.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Book book;
    private Patron patron;

    @BeforeEach
    void setUp() {
        book = new Book(null, "Title", "Author", "ISBN123456", new Date(), true);
        patron = new Patron(null, "John", "Doe", "john@example.com", "password");

        book = bookRepository.save(book);
        patron = patronRepository.save(patron);
    }

    @Test
    void testBorrowAndReturnBook() {
        Transaction transaction = new Transaction();
        transaction.setPatron(patron);
        transaction.setBook(book);
        transaction.setIssueDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusWeeks(2));

        // Save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        assertNotNull(savedTransaction.getId());

        // Return book
        savedTransaction.setReturnDate(LocalDate.now());
        transactionRepository.save(savedTransaction);

        // Verify the book is available again
        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertTrue(updatedBook.isAvailability());
    }

}
