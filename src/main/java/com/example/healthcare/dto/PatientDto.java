package com.example.healthcare.dto;

import java.time.LocalDate;

/**
 * DTO representing the public patient view exposed via the API.
 *
 * <p>This type is intentionally decoupled from the JPA entity to avoid
 * leaking persistence concerns and to allow shaping the API contract
 * independently of the database schema.</p>
 */
public class PatientDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;

    public PatientDto() {
        // Default constructor for JSON (de)serialization
    }

    public PatientDto(Long id, String firstName, String lastName, LocalDate dateOfBirth, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
