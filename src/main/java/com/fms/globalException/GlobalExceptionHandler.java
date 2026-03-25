package com.fms.globalException;


import jakarta.persistence.EntityNotFoundException;

import org.apache.coyote.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fms.util.ErrorDetails;
import com.fms.util.ErrorResponse;


import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Common error response builder
    public ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String code, String message, String details) {
        ErrorDetails errorDetails = new ErrorDetails(code, message, details);
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);
        return new ResponseEntity<>(errorResponse, status);
    }

    // 🔹 1. Body Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed", details);
    }

    // 🔹 2. Param / Path Validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "CONSTRAINT_VIOLATION", "Invalid request parameters", ex.getMessage());
    }

    // 🔹 3. Invalid JSON / Missing Body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_BODY", "Invalid or missing request body", ex.getMostSpecificCause().getMessage());
    }

    // 🔹 4. Missing Query Parameter
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER", "Required parameter missing", ex.getParameterName());
    }

    // 🔹 5. Type Mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", "Invalid parameter type", ex.getName());
    }

 // 🔐 6. Authentication Failures (Spring Security)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                "Authentication failed",
                ex.getMessage()
        );
    }

    // 🔐 7. Authorization Failure (Spring Security)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponse(
                HttpStatus.FORBIDDEN,
                "ACCESS_DENIED",
                "You do not have permission to access this resource",
                ex.getMessage()
        );
    }

    
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "USER_ALREADY_EXISTS",
                ex.getMessage(),
                "A user with the given details already exists"
        );
    }



    // 🗄 8. Database Errors
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity() {
        return buildResponse(HttpStatus.CONFLICT, "DATABASE_ERROR", "Database constraint violation", "Duplicate or invalid data");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound() {
        return buildResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "Requested resource not found", "Entity does not exist");
    }

    // 🌐 9. HTTP Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported() {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "HTTP method not supported", "Use correct HTTP method");
    }

    // 🧠 10. Custom Business Exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleCustomNotFound(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), "Permission denied");
    }

    // ⚠️ 11. Fallback Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex) {
        ex.printStackTrace(); // replace with logger
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Something went wrong", ex.getMessage());
    }
}
