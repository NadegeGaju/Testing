package com.LmsTest.Lab5.repository;

import com.LmsTest.Lab5.entity.Book;
import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByPatronAndBookAndType(Patron patron, Book book, String type);
    void deleteByBookId(Long bookId);
    List<Transaction>findByBookId(Long bookId);
}
