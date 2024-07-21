package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBookByIdWhenBookExists() {
        Book book = new Book(1L, "Title", "Author", "ISBN123456", new Date(), true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.findBookById(1L);
        assertTrue(result.isPresent(), "Book should be present");
        assertEquals("Title", result.get().getTitle(), "Book title should match");
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testFindBookByIdWhenBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.findBookById(1L);
        assertFalse(result.isPresent(), "Book should not be present");
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveBook() {
        Book book = new Book(null, "Title", "Author", "ISBN123456", new Date(), true);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);
        assertNotNull(result, "Saved book should not be null");
        assertEquals("Title", result.getTitle(), "Book title should match");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testSaveBookWithNullFields() {
        Book book = new Book(null, null, null, null, null, true);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);
        assertNotNull(result, "Saved book should not be null");
        assertNull(result.getTitle(), "Book title should be null");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBookWhenBookExists() {
        Book existingBook = new Book(1L, "Old Title", "Old Author", "ISBN123456", new Date(), true);
        Book updatedBook = new Book(null, "New Title", "New Author", "ISBN123456", new Date(), true);

        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(updatedBook)).thenReturn(new Book(1L, "New Title", "New Author", "ISBN123456", new Date(), true));

        Optional<Book> result = bookService.updateBook(1L, updatedBook);
        assertTrue(result.isPresent(), "Updated book should be present");
        assertEquals("New Title", result.get().getTitle(), "Updated book title should match");
        verify(bookRepository, times(1)).save(updatedBook);
    }

    @Test
    void testUpdateBookWhenBookDoesNotExist() {
        Book updatedBook = new Book(null, "New Title", "New Author", "ISBN123456", new Date(), true);

        when(bookRepository.existsById(1L)).thenReturn(false);

        Optional<Book> result = bookService.updateBook(1L, updatedBook);
        assertFalse(result.isPresent(), "Updated book should not be present");
        verify(bookRepository, never()).save(updatedBook);
    }

    @Test
    void testDeleteBookWhenBookExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        boolean result = bookService.deleteBook(1L);
        assertTrue(result, "Book should be deleted successfully");
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookWhenBookDoesNotExist() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        boolean result = bookService.deleteBook(1L);
        assertFalse(result, "Book should not be deleted");
        verify(bookRepository, never()).deleteById(1L);
    }

    @Test
    void testFindAllBooks() {
        Book book1 = new Book(1L, "Title1", "Author1", "ISBN123456", new Date(), true);
        Book book2 = new Book(2L, "Title2", "Author2", "ISBN123456", new Date(), false);
        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAllBooks();
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Size of result should match");
        assertTrue(result.contains(book1), "Result should contain book1");
        assertTrue(result.contains(book2), "Result should contain book2");
        verify(bookRepository, times(1)).findAll();
    }
}
