package com.LmsTest.Lab5.service;


import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.entity.Transaction;
import com.LmsTest.Lab5.repository.BookRepository;
import com.LmsTest.Lab5.repository.PatronRepository;
import com.LmsTest.Lab5.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    // Method to find all transactions
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    // Method to find a transaction by its ID
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    // Method to borrow a book
    public void borrowBook(Long patronId, Long bookId) {
        Optional<Patron> patronOpt = patronRepository.findById(patronId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (patronOpt.isEmpty()) {
            throw new RuntimeException("Patron not found");
        }
        if (bookOpt.isEmpty() || !bookOpt.get().isAvailability()) {
            throw new RuntimeException("Book not available");
        }

        Patron patron = patronOpt.get();
        Book book = bookOpt.get();

        // Create a new transaction for borrowing
        Transaction transaction = new Transaction();
        transaction.setPatron(patron);
        transaction.setBook(book);
        transaction.setIssueDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusWeeks(2)); // Example: 2 weeks due date

        // Mark the book as unavailable
        book.setAvailability(false);
        bookRepository.save(book);

        transactionRepository.save(transaction);
    }

    // Method to return a borrowed book
    public void returnBook(Long transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);

        if (transactionOpt.isEmpty()) {
            throw new RuntimeException("Transaction not found");
        }

        Transaction transaction = transactionOpt.get();
        Book book = transaction.getBook();

        // Mark the book as available
        book.setAvailability(true);
        bookRepository.save(book);

        // Set the return date and update the transaction type
        transaction.setReturnDate(LocalDate.now());
        transaction.setType("return"); // Update the transaction type to "return"
        transactionRepository.save(transaction);
    }

}
