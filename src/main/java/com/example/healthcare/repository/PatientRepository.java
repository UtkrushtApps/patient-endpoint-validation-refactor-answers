package com.example.healthcare.repository;

import com.example.healthcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for {@link Patient} entities.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // No additional methods required for simple findById use case.
}
