package com.example.healthcare.service;

import com.example.healthcare.dto.PatientDto;

/**
 * Service facade for operations related to patients.
 */
public interface PatientService {

    /**
     * Retrieves a patient by id.
     *
     * @param id technical identifier, must be positive
     * @return matching patient DTO
     * @throws com.example.healthcare.exception.PatientNotFoundException if no patient exists for the given id
     */
    PatientDto getPatientById(Long id);
}
