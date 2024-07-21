package com.LmsTest.Lab5.service;

import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PatronServiceIntegrationTest {

    @Autowired
    private PatronService patronService;

    @Autowired
    private PatronRepository patronRepository;

    private Patron patron;

    @BeforeEach
    void setUp() {
        patron = new Patron();
        patron.setFirstName("keza");
        patron.setLastName("aime");
        patron.setEmail("keza.aime@gmail.com");
        patron.setPassword("keza");
        patron = patronRepository.save(patron);
    }

    @Test
    void testFindAllPatrons() {
        List<Patron> patrons = patronService.findAllPatrons();
        assertThat(patrons).isNotEmpty();
        assertThat(patrons).contains(patron);
    }

    @Test
    void testFindPatronById() {
        Optional<Patron> foundPatronOptional = patronService.findPatronById(patron.getId());
        assertThat(foundPatronOptional).isPresent(); // Ensure that the patron is found
        Patron foundPatron = foundPatronOptional.get(); // Retrieve the patron from Optional
        assertThat(foundPatron).isEqualTo(patron); // Verify that it matches the expected patron
    }

    @Test
    void testSavePatron() {
        Patron newPatron = new Patron();
        newPatron.setFirstName("Jane");
        newPatron.setLastName("Smith");
        newPatron.setEmail("jane.smith@example.com");
        Patron savedPatron = patronService.savePatron(newPatron);
        assertThat(savedPatron).isNotNull();
        assertThat(savedPatron.getId()).isNotNull();
        assertThat(savedPatron.getFirstName()).isEqualTo("Jane");
        assertThat(savedPatron.getLastName()).isEqualTo("Smith");
        assertThat(savedPatron.getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void testUpdatePatron() {
        patron.setFirstName("Updated");
        patron.setLastName("Name");
        Patron updatedPatron = patronService.updatePatron(patron.getId(), patron).orElse(null);
        assertThat(updatedPatron).isNotNull(); // Ensure the patron is updated
        assertThat(updatedPatron.getFirstName()).isEqualTo("Updated");
        assertThat(updatedPatron.getLastName()).isEqualTo("Name");
    }

    @Test
    void testDeletePatron() {
        boolean result = patronService.deletePatron(patron.getId());
        assertThat(result).isTrue();
        assertThat(patronService.findPatronById(patron.getId())).isNotPresent();
    }
}
