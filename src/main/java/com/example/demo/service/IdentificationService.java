package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Identification;
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

    public Identification registerIdentification(long id) {
        Identification identification = new Identification();

        // ensuring that no duplication is found
        Optional<Identification> existingId = identificationRepository.findByIdentification(id);
        if (existingId.isPresent()) {
            throw new IllegalStateException("Cet id existe déjà");
        }

        identification.setIdentification(id);

        return identificationRepository.save(identification);
    }

    public List<Identification> findAllFreedIdenfications() {
        return identificationRepository.findIdentificationWithQuery();
    }

    public boolean deleteIdentification(int id) {
        boolean exists = identificationRepository.existsById(id);
        if (!exists) {
            return false; // Return false if the ID doesn't exist
        }

        // If it exists, proceed with the deletion
        identificationRepository.deleteById(id);
        return true; // Return true on successful deletion

    }
}
