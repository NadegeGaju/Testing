package com.LmsTest.Lab5.service;


import com.LmsTest.Lab5.entity.Patron;
import com.LmsTest.Lab5.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    public List<Patron> findAllPatrons() {
        return patronRepository.findAll();
    }

    public Optional<Patron> findPatronById(Long id) {
        return patronRepository.findById(id);
    }

    public Patron savePatron(Patron patron) {
        return patronRepository.save(patron);
    }

    public Optional<Patron> updatePatron(Long id, Patron patron) {
        if (patronRepository.existsById(id)) {
            patron.setId(id);
            return Optional.of(patronRepository.save(patron));
        }
        return Optional.empty();
    }

    public boolean deletePatron(Long id) {
        if (patronRepository.existsById(id)) {
            patronRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
