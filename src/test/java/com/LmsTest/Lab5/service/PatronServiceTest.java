package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatronServiceTest {

    @InjectMocks
    private PatronService patronService;

    @Mock
    private PatronRepository patronRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindPatronByIdWhenPatronExists() {
        Patron patron = new Patron(1L, "John", "Doe", "john@example.com", "password");
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        Optional<Patron> result = patronService.findPatronById(1L);
        assertTrue(result.isPresent(), "Patron should be present");
        assertEquals("John", result.get().getFirstName(), "Patron first name should match");
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testFindPatronByIdWhenPatronDoesNotExist() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Patron> result = patronService.findPatronById(1L);
        assertFalse(result.isPresent(), "Patron should not be present");
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testSavePatron() {
        Patron patron = new Patron(null, "John", "Doe", "john@example.com", "password");
        when(patronRepository.save(patron)).thenReturn(patron);

        Patron result = patronService.savePatron(patron);
        assertNotNull(result, "Saved patron should not be null");
        assertEquals("John", result.getFirstName(), "Patron first name should match");
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    void testUpdatePatronWhenPatronExists() {
        Patron existingPatron = new Patron(1L, "John", "Doe", "john@example.com", "password");
        Patron updatedPatron = new Patron(null, "Jane", "Doe", "jane@example.com", "newpassword");

        when(patronRepository.existsById(1L)).thenReturn(true);
        when(patronRepository.save(updatedPatron)).thenReturn(new Patron(1L, "Jane", "Doe", "jane@example.com", "newpassword"));

        Optional<Patron> result = patronService.updatePatron(1L, updatedPatron);
        assertTrue(result.isPresent(), "Updated patron should be present");
        assertEquals("Jane", result.get().getFirstName(), "Updated patron first name should match");
        verify(patronRepository, times(1)).save(updatedPatron);
    }

    @Test
    void testUpdatePatronWhenPatronDoesNotExist() {
        Patron updatedPatron = new Patron(null, "Jane", "Doe", "jane@example.com", "newpassword");

        when(patronRepository.existsById(1L)).thenReturn(false);

        Optional<Patron> result = patronService.updatePatron(1L, updatedPatron);
        assertFalse(result.isPresent(), "Updated patron should not be present");
        verify(patronRepository, never()).save(updatedPatron);
    }

    @Test
    void testDeletePatronWhenPatronExists() {
        when(patronRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patronRepository).deleteById(1L);

        boolean result = patronService.deletePatron(1L);
        assertTrue(result, "Patron should be deleted successfully");
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatronWhenPatronDoesNotExist() {
        when(patronRepository.existsById(1L)).thenReturn(false);

        boolean result = patronService.deletePatron(1L);
        assertFalse(result, "Patron should not be deleted");
        verify(patronRepository, never()).deleteById(1L);
    }

    @Test
    void testFindAllPatrons() {
        Patron patron1 = new Patron(1L, "John", "Doe", "john@example.com", "password");
        Patron patron2 = new Patron(2L, "Jane", "Doe", "jane@example.com", "password");
        List<Patron> patrons = Arrays.asList(patron1, patron2);

        when(patronRepository.findAll()).thenReturn(patrons);

        List<Patron> result = patronService.findAllPatrons();
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Size of result should match");
        assertTrue(result.contains(patron1), "Result should contain patron1");
        assertTrue(result.contains(patron2), "Result should contain patron2");
        verify(patronRepository, times(1)).findAll();
    }
}
