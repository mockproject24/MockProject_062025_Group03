package com.group3.MockProject.exception;

import com.group3.MockProject.dto.response.ApiResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Centralized exception handling for the application
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/12/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/12/2025      Ngoc Nghia      Create
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles custom AppException instances
     * <p>
     * This is the primary exception handler for application-specific exceptions.
     * It extracts the error code and returns a standardized response with
     * the appropriate HTTP status code and error message.
     * </p>
     *
     * @param exception The AppException instance containing error details
     * @return ResponseEntity with ApiResponse containing error information
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException exception) {
        log.error("AppException occurred: {}", exception.getMessage(), exception);
        
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles validation errors from request body validation
     * <p>
     * Processes validation errors that occur when request body validation fails.
     * Extracts field-specific error messages and attempts to map them to
     * appropriate ErrorCode values, falling back to INVALID_KEY if no match is found.
     * </p>
     *
     * @param exception The MethodArgumentNotValidException containing validation errors
     * @return ResponseEntity with ApiResponse containing validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException exception) {
        log.error("Validation error occurred: {}", exception.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message("Validation failed: " + errors.toString())
                .result(errors)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles authentication-related exceptions
     * <p>
     * Processes authentication failures such as invalid credentials or
     * authentication token issues. Converts these to appropriate AppException
     * instances with AUTHENTICATION_FAILED error code.
     * </p>
     *
     * @param exception The AuthenticationException that occurred
     * @return ResponseEntity with ApiResponse containing authentication error details
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exception) {
        log.error("Authentication error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles access denied exceptions
     * <p>
     * Processes authorization failures when authenticated users lack sufficient
     * permissions to access requested resources. Converts these to ACCESS_DENIED
     * error code.
     * </p>
     *
     * @param exception The AccessDeniedException that occurred
     * @return ResponseEntity with ApiResponse containing access denied error details
     */
    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class, java.nio.file.AccessDeniedException.class})
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(Exception exception) {
        log.error("Access denied error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles entity not found exceptions
     * <p>
     * Processes JPA EntityNotFoundException instances and converts them to
     * appropriate error responses. The specific error code depends on the
     * entity type that was not found.
     * </p>
     *
     * @param exception The EntityNotFoundException that occurred
     * @return ResponseEntity with ApiResponse containing not found error details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error("Entity not found error occurred: {}", exception.getMessage());
        
        // Default to generic not found, but could be enhanced to detect specific entity types
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        String message = exception.getMessage();
        
        // Try to determine specific entity type from message
        if (message.toLowerCase().contains("user")) {
            errorCode = ErrorCode.USER_NOT_EXISTED;
        } else if (message.toLowerCase().contains("case")) {
            errorCode = ErrorCode.CASE_NOT_EXISTED;
        } else if (message.toLowerCase().contains("suspect")) {
            errorCode = ErrorCode.SUSPECT_NOT_EXISTED;
        } else if (message.toLowerCase().contains("interview")) {
            errorCode = ErrorCode.INTERVIEW_NOT_FOUND;
        } else if (message.toLowerCase().contains("evidence")) {
            errorCode = ErrorCode.EVIDENCE_NOT_FOUND;
        }
        
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles entity already exists exceptions
     * <p>
     * Processes JPA EntityExistsException instances and converts them to
     * appropriate conflict error responses.
     * </p>
     *
     * @param exception The EntityExistsException that occurred
     * @return ResponseEntity with ApiResponse containing conflict error details
     */
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityExistsException(EntityExistsException exception) {
        log.error("Entity already exists error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.USER_EXISTED; // Default, could be enhanced
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles data integrity violation exceptions
     * <p>
     * Processes database constraint violations and converts them to appropriate
     * error responses. This typically occurs when database constraints are violated.
     * </p>
     *
     * @param exception The DataIntegrityViolationException that occurred
     * @return ResponseEntity with ApiResponse containing database error details
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error("Data integrity violation occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.DATABASE_ERROR;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message("Database constraint violation: " + exception.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles constraint violation exceptions
     * <p>
     * Processes bean validation constraint violations and converts them to
     * appropriate error responses.
     * </p>
     *
     * @param exception The ConstraintViolationException that occurred
     * @return ResponseEntity with ApiResponse containing constraint violation error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("Constraint violation occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message("Constraint violation: " + exception.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles missing request parameter exceptions
     * <p>
     * Processes cases where required request parameters are missing from the HTTP request.
     * </p>
     *
     * @param exception The MissingServletRequestParameterException that occurred
     * @return ResponseEntity with ApiResponse containing missing parameter error details
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("Missing request parameter error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message("Missing required parameter: " + exception.getParameterName())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles method argument type mismatch exceptions
     * <p>
     * Processes cases where request parameters cannot be converted to the expected type.
     * </p>
     *
     * @param exception The MethodArgumentTypeMismatchException that occurred
     * @return ResponseEntity with ApiResponse containing type mismatch error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("Method argument type mismatch error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message("Invalid parameter type for: " + exception.getName())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles HTTP message not readable exceptions
     * <p>
     * Processes cases where the HTTP request body cannot be read or parsed.
     * </p>
     *
     * @param exception The HttpMessageNotReadableException that occurred
     * @return ResponseEntity with ApiResponse containing message parsing error details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("HTTP message not readable error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message("Invalid request body format")
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles maximum upload size exceeded exceptions
     * <p>
     * Processes cases where uploaded files exceed the maximum allowed size.
     * </p>
     *
     * @param exception The MaxUploadSizeExceededException that occurred
     * @return ResponseEntity with ApiResponse containing file size error details
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<?>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        log.error("Maximum upload size exceeded error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.FILE_TOO_LARGE;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    /**
     * Handles no handler found exceptions
     * <p>
     * Processes cases where no handler is found for the requested URL.
     * </p>
     *
     * @param exception The NoHandlerFoundException that occurred
     * @return ResponseEntity with ApiResponse containing not found error details
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        log.error("No handler found error occurred: {}", exception.getMessage());
        
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<?> response = ApiResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Endpoint not found: " + exception.getRequestURL())
                .result(null)
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles MockProjectException instances (legacy support)
     * <p>
     * Provides backward compatibility for existing MockProjectException instances
     * while migrating to the new AppException and ErrorCode system.
     * </p>
     *
     * @param exception The MockProjectException that occurred
     * @return ResponseEntity with ApiResponse containing legacy error details
     */
    @ExceptionHandler(MockProjectException.class)
    public ResponseEntity<ApiResponse<?>> handleMockProjectException(MockProjectException exception) {
        log.error("MockProjectException occurred: {}", exception.getMessage());
        
        ApiResponse<?> response = ApiResponse.builder()
                .code(exception.getStatus())
                .message(exception.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(HttpStatus.valueOf(exception.getStatus())).body(response);
    }

    /**
     * Handles all other uncategorized exceptions
     * <p>
     * This is the catch-all handler for any exceptions not specifically handled
     * by other handlers. It provides a generic error response to avoid exposing
     * sensitive system information while logging the full error details.
     * </p>
     *
     * @param exception The generic Exception that occurred
     * @param request   The web request context
     * @return ResponseEntity with ApiResponse containing generic error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception exception, WebRequest request) {
        log.error("Uncategorized exception occurred: {}", exception.getMessage(), exception);
        
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(null)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}
