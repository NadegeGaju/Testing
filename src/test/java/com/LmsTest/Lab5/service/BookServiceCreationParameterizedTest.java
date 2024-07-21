package com.LmsTest.Lab5.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
class BookServiceCreationParameterizedTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        // No initial setup needed for this test
    }

    @ParameterizedTest
    @MethodSource("provideBookCreationData")
    void testCreateBook(String title, String author, String isbn, Date publishedDate, boolean availability) {
        // Convert Date to Timestamp for comparison
        Timestamp expectedTimestamp = new Timestamp(publishedDate.getTime());

        // Create a new Book object with provided parameters
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublishedDate(publishedDate);
        book.setAvailability(availability);

        // Save the book using the service
        Book createdBook = bookService.saveBook(book);

        // Fetch the created book
        Optional<Book> fetchedBook = bookRepository.findById(createdBook.getId());
        assertThat(fetchedBook).isPresent();
        Book fetched = fetchedBook.get();
        assertThat(fetched.getTitle()).isEqualTo(title);
        assertThat(fetched.getAuthor()).isEqualTo(author);
        assertThat(fetched.getIsbn()).isEqualTo(isbn);

        // Convert the fetched Timestamp to Date for comparison
        Timestamp fetchedTimestamp = new Timestamp(fetched.getPublishedDate().getTime());
        assertThat(fetchedTimestamp).isEqualTo(expectedTimestamp);

        assertThat(fetched.isAvailability()).isEqualTo(availability);
    }

    static Stream<Arguments> provideBookCreationData() {
        return Stream.of(
                arguments("Book Title 1", "Author 1", "978-3-16-148410-0", toDate(LocalDate.of(2024, 1, 1)), true),
                arguments("Book Title 2", "Author 2", "978-1-61-729223-4", toDate(LocalDate.of(2024, 6, 15)), false)
        );
    }

    // Helper method to convert LocalDate to Date
    private static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
