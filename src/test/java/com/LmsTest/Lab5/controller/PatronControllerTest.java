package com.LmsTest.Lab5.controller;

import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PatronControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private Patron patron;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patron = new Patron(1L, "John","Doe", "john.doe@example.com", "passowrd");
    }

    @Test
    void testGetAllPatrons() {
        when(patronService.findAllPatrons()).thenReturn(Collections.singletonList(patron));

        ResponseEntity<List<Patron>> response = patronController.getAllPatrons();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.singletonList(patron), response.getBody());
    }

    @Test
    void testGetPatronById() {
        when(patronService.findPatronById(anyLong())).thenReturn(Optional.of(patron));

        ResponseEntity<Patron> response = patronController.getPatronById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(patron, response.getBody());
    }

    @Test
    void testCreatePatron() {
        when(patronService.savePatron(any(Patron.class))).thenReturn(patron);

        ResponseEntity<Patron> response = patronController.createPatron(patron);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(patron, response.getBody());
    }

    @Test
    void testUpdatePatron() {
        when(patronService.updatePatron(anyLong(), any(Patron.class))).thenReturn(Optional.of(patron));

        ResponseEntity<Patron> response = patronController.updatePatron(1L, patron);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(patron, response.getBody());
    }

    @Test
    void testDeletePatron() {
        when(patronService.deletePatron(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = patronController.deletePatron(1L);
        assertEquals(204, response.getStatusCodeValue());
    }
}
