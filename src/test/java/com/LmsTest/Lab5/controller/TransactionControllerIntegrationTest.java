package com.LmsTest.Lab5.controller;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.entity.Transaction;
import com.LmsTest.Lab5.repository.BookRepository;
import com.LmsTest.Lab5.repository.PatronRepository;
import com.LmsTest.Lab5.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BookRepository bookRepository;

    private Transaction transaction;
    private Patron patron;
    private Book book;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        patronRepository.deleteAll();
        bookRepository.deleteAll();

        patron = new Patron(1L, "Keza","Joh" ,"keza.joh@example.com","keza");
        book = new Book(1L, "Sample Book", "Sample Author", "ISBN123456", new Date(), true);

        patron = patronRepository.save(patron);
        book = bookRepository.save(book);

        transaction = new Transaction(null, patron, book, LocalDate.now(), LocalDate.now().plusDays(14), "borrow", null);
        transaction = transactionRepository.save(transaction);
    }

    @Test
    void testGetAllTransactions() throws Exception {
        mockMvc.perform(get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(transaction.getId()))
                .andExpect(jsonPath("$[0].patron.id").value(patron.getId()))
                .andExpect(jsonPath("$[0].book.id").value(book.getId()));
    }

    @Test
    void testGetTransactionById() throws Exception {
        mockMvc.perform(get("/transactions/{id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.patron.id").value(patron.getId()))
                .andExpect(jsonPath("$.book.id").value(book.getId()));
    }

    @Test
    void testBorrowBook() throws Exception {
        mockMvc.perform(post("/transactions/borrow")
                        .param("patronId", String.valueOf(patron.getId()))
                        .param("bookId", String.valueOf(book.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testReturnBook() throws Exception {
        mockMvc.perform(post("/transactions/return")
                        .param("transactionId", String.valueOf(transaction.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
