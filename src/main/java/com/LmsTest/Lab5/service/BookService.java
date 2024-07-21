package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.repository.BookRepository;
import com.LmsTest.Lab5.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            return Optional.of(bookRepository.save(book));
        }
        return Optional.empty();
    }

    public boolean deleteBook(Long bookId) {
        // First delete all transactions related to the book
        transactionRepository.deleteByBookId(bookId);

        // Then delete the book
        bookRepository.deleteById(bookId);
        return false;
    }

}
