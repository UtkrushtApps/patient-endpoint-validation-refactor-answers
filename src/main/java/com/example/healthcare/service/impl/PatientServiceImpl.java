package com.example.healthcare.service.impl;

import com.example.healthcare.dto.PatientDto;
import com.example.healthcare.entity.Patient;
import com.example.healthcare.exception.PatientNotFoundException;
import com.example.healthcare.mapper.PatientMapper;
import com.example.healthcare.repository.PatientRepository;
import com.example.healthcare.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link PatientService}.
 */
@Service
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = new PatientMapper();
    }

    @Override
    public PatientDto getPatientById(Long id) {
        log.debug("Looking up patient with id={}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patient with id={} not found", id);
                    return new PatientNotFoundException(id);
                });

        return patientMapper.toDto(patient);
    }
}
