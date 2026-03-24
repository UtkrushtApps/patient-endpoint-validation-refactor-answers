package com.example.healthcare.mapper;

import com.example.healthcare.dto.PatientDto;
import com.example.healthcare.entity.Patient;

/**
 * Mapper responsible for converting between {@link Patient} entities and
 * {@link PatientDto} DTOs.
 */
public class PatientMapper {

    /**
     * Maps a {@link Patient} JPA entity to a {@link PatientDto}.
     *
     * @param patient entity instance, must not be {@code null}
     * @return mapped DTO instance
     */
    public PatientDto toDto(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("patient must not be null");
        }

        return new PatientDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender()
        );
    }
}
