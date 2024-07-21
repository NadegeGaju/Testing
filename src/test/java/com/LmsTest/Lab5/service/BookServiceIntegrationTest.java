package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Sample Author");
        book.setIsbn("ISBN123456");
        book.setPublishedDate(new Date());
        book.setAvailability(true);
        book = bookRepository.save(book);
    }

    @Test
    void testFindAllBooks() {
        List<Book> books = bookService.findAllBooks();
        assertThat(books).isNotEmpty();
        assertThat(books).contains(book);
    }

    @Test
    void testFindBookById() {
        Optional<Book> foundBookOptional = bookService.findBookById(book.getId());
        assertThat(foundBookOptional).isPresent(); // Ensure that the book is found
        Book foundBook = foundBookOptional.get(); // Retrieve the book from Optional
        assertThat(foundBook).isEqualTo(book); // Verify that it matches the expected book
    }

    @Test
    void testSaveBook() {
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setIsbn("ISBN654321");
        newBook.setPublishedDate(new Date());
        newBook.setAvailability(true);
        Book savedBook = bookService.saveBook(newBook);
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");
        assertThat(savedBook.getAuthor()).isEqualTo("New Author");
    }

    @Test
    void testUpdateBook() {
        book.setTitle("Updated Book Title");
        book.setAuthor("Updated Author");
        Book updatedBook = bookService.updateBook(book.getId(), book).orElse(null);
        assertThat(updatedBook).isNotNull(); // Ensure the book is updated
        assertThat(updatedBook.getTitle()).isEqualTo("Updated Book Title");
        assertThat(updatedBook.getAuthor()).isEqualTo("Updated Author");
    }

    @Test
    void testDeleteBook() {
        boolean result = bookService.deleteBook(book.getId());
        assertThat(result).isTrue();
        assertThat(bookService.findBookById(book.getId())).isNotPresent();
    }
}
