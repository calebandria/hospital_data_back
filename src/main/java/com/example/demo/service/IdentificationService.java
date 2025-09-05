package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Identification;
import com.example.demo.model.Role;
import com.example.demo.repository.IdentificationRepository;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class IdentificationService {
    @Autowired
    private IdentificationRepository identificationRepository;

    public IdentificationService(IdentificationRepository identificationRepository) {
        this.identificationRepository = identificationRepository;
    }

    public Identification registerIdentification(long id, Role role) {
        Identification identification = new Identification();

        // ensuring that no duplication is found
        Optional<Identification> existingId = identificationRepository.findByIdentification(id);
        if (existingId.isPresent()) {
            throw new IllegalStateException("Cet id existe déjà");
        }

        identification.setIdentification(id);
        identification.setRole(role);

        return identificationRepository.save(identification);
    }

    public List<Identification> findAllFreedIdenfications() {
        return identificationRepository.findIdentificationWithQuery();
    }

    public boolean deleteIdentification(int id) {
        boolean exists = identificationRepository.existsById(id);
        if (!exists) {
            return false;
        }

        identificationRepository.deleteById(id);
        return true;
    }

    public Identification findSingleFreeIdentification(long code) {
        try {
            Identification foundIdentification = identificationRepository.findByIdentification(code)
                    .orElseThrow(
                            () -> new NoSuchElementException("No existing identificaton! Please contact the admin"));

            if (foundIdentification.getUser() == null) {
                return foundIdentification;
            } else {
                System.out.println(foundIdentification.getUser());
                return null;
            }

        } catch (NoSuchElementException err) {
            throw (err);

        }

    }
}
