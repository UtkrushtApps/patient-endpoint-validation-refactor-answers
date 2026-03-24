package com.example.healthcare.exception;

/**
 * Standard error response payload for all API errors.
 *
 * <p>Examples:</p>
 * <pre>
 * {"code":"PATIENT_NOT_FOUND","message":"Patient with id=42 was not found"}
 * {"code":"INVALID_PATH_VARIABLE","message":"Parameter 'patientId' has invalid value 'abc'"}
 * </pre>
 */
public class ApiErrorResponse {

    private final String code;
    private final String message;

    public ApiErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiErrorResponse of(ErrorCode errorCode, String message) {
        return new ApiErrorResponse(errorCode.getCode(), message);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
