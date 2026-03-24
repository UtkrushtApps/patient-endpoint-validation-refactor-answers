# Solution Steps

1. Create a Patient DTO to decouple the API contract from the JPA entity:
- Add `PatientDto` with fields like id, firstName, lastName, dateOfBirth, gender.
- Provide a no-args constructor for JSON frameworks and a full-args constructor for convenience.
- Generate getters and setters for all fields.

2. Introduce or confirm the JPA entity representing patients:
- Create `Patient` as a JPA `@Entity` mapped to table `patients`.
- Add `id` as `@Id` with generated value and columns for firstName, lastName, dateOfBirth, and gender.
- Provide standard getters and setters and a no-arg constructor for JPA.

3. Expose a persistence abstraction for patients:
- Create `PatientRepository` extending `JpaRepository<Patient, Long>` and annotate it with `@Repository`.
- Rely on the inherited `findById` for patient lookups; no custom queries are required.

4. Add a mapper to convert entities to DTOs:
- Implement `PatientMapper` with a `toDto(Patient patient)` method.
- Validate the input (e.g., throw `IllegalArgumentException` if the entity is null).
- Map all relevant fields from `Patient` to `PatientDto`.

5. Define a patient service abstraction:
- Create `PatientService` interface with a `PatientDto getPatientById(Long id)` method.
- Document that it throws a not-found exception when the patient does not exist.

6. Implement the patient service with proper not-found handling:
- Create `PatientServiceImpl` annotated with `@Service` and `@Transactional(readOnly = true)`.
- Inject `PatientRepository` and instantiate or inject `PatientMapper`.
- In `getPatientById`, call `patientRepository.findById(id)`.
- If present, map the entity to `PatientDto` and return it.
- If absent, log a warning and throw a custom `PatientNotFoundException` containing the id.

7. Introduce error code semantics and a domain-specific not-found exception:
- Create `ErrorCode` enum with values like `PATIENT_NOT_FOUND`, `INVALID_PATH_VARIABLE`, `VALIDATION_ERROR`, and `INTERNAL_SERVER_ERROR`, each exposing a string `code`.
- Implement `PatientNotFoundException` extending `RuntimeException`, keeping the `patientId` as a field and building a clear message like "Patient with id=<id> was not found".

8. Standardize error response payloads:
- Create `ApiErrorResponse` with immutable fields `code` and `message`.
- Add a constructor and getters.
- Provide a static factory `of(ErrorCode errorCode, String message)` to build responses from an error code and message.

9. Centralize exception handling and response formatting:
- Create `GlobalExceptionHandler` annotated with `@RestControllerAdvice`.
- Handle `PatientNotFoundException` by returning HTTP 404 with an `ApiErrorResponse` built from `ErrorCode.PATIENT_NOT_FOUND` and the exception message.
- Handle `MethodArgumentTypeMismatchException` (e.g., non-numeric IDs) by returning HTTP 400 and building an `ApiErrorResponse` with `INVALID_PATH_VARIABLE` and a message stating which parameter has an invalid value.
- Handle `ConstraintViolationException` for validation errors (e.g., negative IDs) by aggregating constraint messages and returning HTTP 400 with `VALIDATION_ERROR`.
- Optionally handle `MethodArgumentNotValidException` for body validation in a similar way.
- Add a catch-all `@ExceptionHandler(Exception.class)` that logs the error and returns HTTP 500 with `INTERNAL_SERVER_ERROR` and a generic message.

10. Refactor the REST controller to use validation, DTOs, and the service layer:
- Create or update `PatientController` with `@RestController` and `@RequestMapping("/api/healthcare/patients")`.
- Annotate the class with `@Validated` to enable bean validation on method parameters.
- Implement `@GetMapping("/{patientId}")` with a method `getPatientById` accepting `@PathVariable("patientId") @Min(1) Long patientId`.
- Inside the method, log the request, delegate to `patientService.getPatientById(patientId)`, and return the `PatientDto` in a `ResponseEntity.ok()`.
- Do not handle errors directly in the controller; rely on the global exception handler.

11. Verify behavior against the requirements:
- For a non-numeric `patientId` (e.g., `/api/healthcare/patients/abc`), confirm that Spring throws `MethodArgumentTypeMismatchException` and the global handler returns HTTP 400 with `{ "code": "INVALID_PATH_VARIABLE", "message": "..." }`.
- For a numeric but invalid `patientId` (e.g., `0` or negative), confirm that validation triggers a `ConstraintViolationException` and you get HTTP 400 with `{ "code": "VALIDATION_ERROR", "message": "patientId must be a positive number" }`.
- For a non-existent but valid `patientId`, confirm `PatientServiceImpl` throws `PatientNotFoundException`, resulting in HTTP 404 with `{ "code": "PATIENT_NOT_FOUND", "message": "Patient with id=<id> was not found" }`.
- For an existing patient, verify that HTTP 200 is returned and the body is a `PatientDto` JSON without exposing JPA internals.

