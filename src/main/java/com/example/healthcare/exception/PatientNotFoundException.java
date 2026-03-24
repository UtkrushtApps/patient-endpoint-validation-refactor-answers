package com.example.healthcare.exception;

/**
 * Exception thrown when a patient is requested by id but does not exist.
 */
public class PatientNotFoundException extends RuntimeException {

    private final Long patientId;

    public PatientNotFoundException(Long patientId) {
        super("Patient with id=" + patientId + " was not found");
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }
}
