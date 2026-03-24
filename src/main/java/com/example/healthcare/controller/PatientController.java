package com.example.healthcare.controller;

import com.example.healthcare.dto.PatientDto;
import com.example.healthcare.service.PatientService;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing patient resources.
 */
@RestController
@RequestMapping("/api/healthcare/patients")
@Validated
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Returns a single patient by id.
     *
     * <p>Validation rules:
     * <ul>
     *   <li>patientId must be a positive number</li>
     *   <li>Non-numeric path values are rejected by global exception handling</li>
     * </ul>
     *
     * @param patientId technical identifier of the patient
     * @return HTTP 200 with {@link PatientDto} if found
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(
            @PathVariable("patientId")
            @Min(value = 1, message = "patientId must be a positive number") Long patientId) {

        log.info("Handling request to fetch patient with id={}", patientId);
        PatientDto patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(patient);
    }
}
