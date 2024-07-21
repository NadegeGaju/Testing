package com.LmsTest.Lab5.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Transaction;
import com.LmsTest.Lab5.repository.BookRepository;
import com.LmsTest.Lab5.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BookServiceRegressionTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        // Initialize or clean up data before each test
        bookRepository.deleteAll(); // Example cleanup
    }

    @Test
    void testBookCreation() {
        // Test case for verifying book creation functionality
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Sample Author");
        book.setIsbn("123-456-789");
        book.setPublishedDate(new Date());
        book.setAvailability(true);

        Book createdBook = bookService.saveBook(book);
        assertNotNull(createdBook.getId(), "Book ID should not be null");

        Optional<Book> fetchedBook = bookRepository.findById(createdBook.getId());
        assertTrue(fetchedBook.isPresent(), "Book should be present in the repository");
        assertEquals(book.getTitle(), fetchedBook.get().getTitle(), "Title should match");
        // Additional assertions can be added here to verify other fields
    }

    @Test
    void testBookUpdate() {
        // Test case for verifying book update functionality
        // 1. Create and save a book
        Book book = new Book();
        book.setTitle("Old Title");
        book.setAuthor("Old Author");
        book.setIsbn("987-654-321");
        book.setPublishedDate(new Date());
        book.setAvailability(true);
        Book createdBook = bookService.saveBook(book);

        // 2. Update the book
        createdBook.setTitle("Updated Title");
        createdBook.setAuthor("Updated Author");
        bookService.saveBook(createdBook);

        // 3. Fetch the book again and verify the updated fields
        Optional<Book> updatedBook = bookRepository.findById(createdBook.getId());
        assertTrue(updatedBook.isPresent(), "Book should be present in the repository");
        assertEquals("Updated Title", updatedBook.get().getTitle(), "Updated title should match");
        assertEquals("Updated Author", updatedBook.get().getAuthor(), "Updated author should match");
    }

    @Test
    void testBookDeletion() {
        // Create and save a book with related transactions
        Book book = new Book();
        book.setTitle("Book to Delete");
        book.setAuthor("Author");
        book.setIsbn("111-222-333");
        book.setPublishedDate(new Date());
        book.setAvailability(true);
        Book createdBook = bookRepository.save(book);

        // Create and save related transactions
        Transaction transaction = new Transaction();
        transaction.setBook(createdBook);
        transactionRepository.save(transaction);

        // Attempt to delete the book
        bookService.deleteBook(createdBook.getId());

        // Verify that the book and related transactions are deleted
        Optional<Book> deletedBook = bookRepository.findById(createdBook.getId());
        assertTrue(deletedBook.isEmpty(), "Book should not be present in the repository");

        List<Transaction> transactions = transactionRepository.findByBookId(createdBook.getId());
        assertTrue(transactions.isEmpty(), "Transactions related to the book should be deleted");
    }
}
