package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Identification;

@Repository
public interface IdentificationRepository extends JpaRepository<Identification, Integer> {
    @Query("SELECT i FROM Identification i WHERE i.user IS NULL")
    List<Identification> findIdentificationWithQuery();

    Optional<Identification> findByIdentification(long id);
}
