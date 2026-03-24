package com.example.healthcare.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Centralized exception handling for all controllers.
 *
 * <p>Ensures that all errors are returned as structured JSON with
 * {@code code} and {@code message} fields.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePatientNotFound(PatientNotFoundException ex) {
        log.info("Patient not found: id={}", ex.getPatientId());
        ApiErrorResponse body = ApiErrorResponse.of(ErrorCode.PATIENT_NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        Object value = ex.getValue();
        String valueAsString = value != null ? value.toString() : "null";

        String message = String.format("Parameter '%s' has invalid value '%s'", parameterName, valueAsString);
        log.debug("Invalid method argument: {}", message, ex);

        ApiErrorResponse body = ApiErrorResponse.of(ErrorCode.INVALID_PATH_VARIABLE, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        String message;
        if (violations == null || violations.isEmpty()) {
            message = "Request parameters failed validation";
        } else {
            message = violations.stream()
                    .map(violation -> {
                        String path = violation.getPropertyPath() != null
                                ? violation.getPropertyPath().toString()
                                : "";
                        return path + ": " + violation.getMessage();
                    })
                    .collect(Collectors.joining(", "));
        }

        log.debug("Constraint violation: {}", message, ex);

        ApiErrorResponse body = ApiErrorResponse.of(ErrorCode.VALIDATION_ERROR, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.debug("Method argument not valid: {}", message, ex);

        ApiErrorResponse body = ApiErrorResponse.of(ErrorCode.VALIDATION_ERROR, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception while processing request", ex);
        ApiErrorResponse body = ApiErrorResponse.of(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please contact support if the problem persists."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
