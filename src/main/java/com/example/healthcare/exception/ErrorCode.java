package com.example.healthcare.exception;

/**
 * Enumerates high-level API error codes.
 */
public enum ErrorCode {

    /** Resource could not be found. */
    PATIENT_NOT_FOUND("PATIENT_NOT_FOUND"),

    /** Path variable or request parameter contains an invalid value. */
    INVALID_PATH_VARIABLE("INVALID_PATH_VARIABLE"),

    /** Bean validation or other client-side input error. */
    VALIDATION_ERROR("VALIDATION_ERROR"),

    /** Unexpected server-side failure. */
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
