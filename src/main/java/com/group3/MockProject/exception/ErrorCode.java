package com.group3.MockProject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/10/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/10/2025      Ngoc Nghia      Create
 */
@Getter
public enum ErrorCode {
    
    // ================= SYSTEM ERROR (9000 - 9999) =================
    
    /**
     * General uncategorized exception for unexpected errors
     * <p>
     * Used when an unexpected error occurs that doesn't fit into other categories.
     * This should be used sparingly and specific error codes should be preferred.
     * </p>
     */
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    
    /**
     * Invalid key or parameter provided in request
     * <p>
     * Used when a request contains invalid keys, parameters, or malformed data
     * that cannot be processed by the system.
     * </p>
     */
    INVALID_KEY(9001, "Invalid key provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Database connection or transaction error
     * <p>
     * Used when database operations fail due to connection issues or
     * transaction rollback scenarios.
     * </p>
     */
    DATABASE_ERROR(9002, "Database operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    
    /**
     * External service communication error
     * <p>
     * Used when communication with external services fails or times out.
     * </p>
     */
    EXTERNAL_SERVICE_ERROR(9003, "External service communication failed", HttpStatus.SERVICE_UNAVAILABLE),

    // ================= AUTHENTICATION & AUTHORIZATION (1000 - 1099) ========================
    
    /**
     * Authentication failed due to invalid credentials
     * <p>
     * Used when user provides incorrect username or password during login.
     * </p>
     */
    AUTHENTICATION_FAILED(1001, "Authentication failed - invalid credentials", HttpStatus.UNAUTHORIZED),
    
    /**
     * Access token is invalid or expired
     * <p>
     * Used when JWT token validation fails or token has expired.
     * </p>
     */
    TOKEN_INVALID(1002, "Invalid or expired token", HttpStatus.UNAUTHORIZED),
    
    /**
     * User does not have permission to access the resource
     * <p>
     * Used when authenticated user lacks sufficient privileges for the requested operation.
     * </p>
     */
    ACCESS_DENIED(1003, "Access denied - insufficient permissions", HttpStatus.FORBIDDEN),
    
    /**
     * User session has expired
     * <p>
     * Used when user session times out and requires re-authentication.
     * </p>
     */
    SESSION_EXPIRED(1004, "Session expired - please login again", HttpStatus.UNAUTHORIZED),

    // ================= CASE MANAGEMENT (1100 - 1199) ========================
    
    /**
     * Case with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify a case that cannot be found
     * in the database.
     * </p>
     */
    CASE_NOT_EXISTED(1101, "Case not found", HttpStatus.NOT_FOUND),
    
    /**
     * Invalid pagination parameters for case listing
     * <p>
     * Used when page number or page size parameters are invalid (e.g., negative values).
     * </p>
     */
    CASE_PAGE_SIZE(1102, "Page and pageSize must be greater than 0", HttpStatus.BAD_REQUEST),
    
    /**
     * Case creation failed due to invalid data
     * <p>
     * Used when case creation fails due to missing required fields or invalid data.
     * </p>
     */
    CASE_CREATION_FAILED(1103, "Case creation failed - invalid data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Case update failed due to invalid data or permissions
     * <p>
     * Used when case update operation fails due to invalid data or insufficient permissions.
     * </p>
     */
    CASE_UPDATE_FAILED(1104, "Case update failed", HttpStatus.BAD_REQUEST),
    
    /**
     * Case deletion failed due to constraints or permissions
     * <p>
     * Used when case deletion fails due to existing dependencies or insufficient permissions.
     * </p>
     */
    CASE_DELETE_FAILED(1105, "Case deletion failed", HttpStatus.BAD_REQUEST),

    // ================= SUSPECT MANAGEMENT (1200 - 1299) =====================
    
    /**
     * Suspect with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify a suspect that cannot be found
     * in the database.
     * </p>
     */
    SUSPECT_NOT_EXISTED(1201, "Suspect not found", HttpStatus.NOT_FOUND),
    
    /**
     * Suspect full name is required and cannot be blank
     * <p>
     * Used when creating or updating a suspect with missing or blank full name.
     * </p>
     */
    SUSPECT_INVALID_NAME(1202, "Suspect full name is required and must not be blank", HttpStatus.BAD_REQUEST),
    
    /**
     * Suspect address is required and cannot be blank
     * <p>
     * Used when creating or updating a suspect with missing or blank address.
     * </p>
     */
    SUSPECT_INVALID_ADDRESS(1203, "Suspect address is required and must not be blank", HttpStatus.BAD_REQUEST),
    
    /**
     * Suspect creation failed due to invalid data
     * <p>
     * Used when suspect creation fails due to missing required fields or invalid data.
     * </p>
     */
    SUSPECT_CREATION_FAILED(1204, "Suspect creation failed - invalid data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Suspect update failed due to invalid data or permissions
     * <p>
     * Used when suspect update operation fails due to invalid data or insufficient permissions.
     * </p>
     */
    SUSPECT_UPDATE_FAILED(1205, "Suspect update failed", HttpStatus.BAD_REQUEST),

    // ================= USER MANAGEMENT (1300 - 1399) ========================
    
    /**
     * User with specified ID or username does not exist
     * <p>
     * Used when attempting to retrieve or modify a user that cannot be found
     * in the database.
     * </p>
     */
    USER_NOT_EXISTED(1301, "User not found", HttpStatus.NOT_FOUND),
    
    /**
     * User with specified username already exists
     * <p>
     * Used when attempting to create a user with a username that already exists
     * in the system.
     * </p>
     */
    USER_EXISTED(1302, "User already exists", HttpStatus.CONFLICT),
    
    /**
     * User authentication is required for this operation
     * <p>
     * Used when user is not authenticated but tries to access protected resources.
     * </p>
     */
    USER_UNAUTHENTICATED(1303, "Authentication required", HttpStatus.UNAUTHORIZED),
    
    /**
     * User registration failed due to invalid data
     * <p>
     * Used when user registration fails due to missing required fields or invalid data.
     * </p>
     */
    USER_REGISTRATION_FAILED(1304, "User registration failed - invalid data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * User profile update failed
     * <p>
     * Used when user profile update operation fails due to invalid data or constraints.
     * </p>
     */
    USER_UPDATE_FAILED(1305, "User profile update failed", HttpStatus.BAD_REQUEST),

    // ================= FILE UPLOAD (1400 - 1499) =================
    
    /**
     * Uploaded file is empty or null
     * <p>
     * Used when file upload request contains no file data or empty file.
     * </p>
     */
    FILE_EMPTY(1401, "File is empty - please upload a valid file", HttpStatus.BAD_REQUEST),
    
    /**
     * File extension is not allowed
     * <p>
     * Used when uploaded file has an extension that is not permitted by the system.
     * </p>
     */
    FILE_INVALID_EXTENSION(1402, "Invalid file extension - only allowed formats are permitted", HttpStatus.BAD_REQUEST),
    
    /**
     * File upload operation failed
     * <p>
     * Used when file upload fails due to server issues, storage problems, or I/O errors.
     * </p>
     */
    FILE_UPLOAD_FAILED(1403, "File upload failed - please try again", HttpStatus.INTERNAL_SERVER_ERROR),
    
    /**
     * File size exceeds maximum allowed limit
     * <p>
     * Used when uploaded file exceeds the maximum size limit configured in the system.
     * </p>
     */
    FILE_TOO_LARGE(1404, "File size exceeds maximum allowed limit", HttpStatus.BAD_REQUEST),
    
    /**
     * File download failed
     * <p>
     * Used when file download operation fails due to file not found or I/O errors.
     * </p>
     */
    FILE_DOWNLOAD_FAILED(1405, "File download failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================= INTERVIEW MANAGEMENT (1500 - 1599) ===================
    
    /**
     * Interview with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify an interview that cannot be found
     * in the database.
     * </p>
     */
    INTERVIEW_NOT_FOUND(1501, "Interview not found", HttpStatus.NOT_FOUND),
    
    /**
     * Interviewer with specified ID does not exist
     * <p>
     * Used when attempting to assign an interviewer that cannot be found in the system.
     * </p>
     */
    INTERVIEWER_NOT_FOUND(1502, "Interviewer not found", HttpStatus.NOT_FOUND),
    
    /**
     * Interviewee with specified ID does not exist
     * <p>
     * Used when attempting to assign an interviewee that cannot be found in the system.
     * </p>
     */
    INTERVIEWEE_NOT_FOUND(1503, "Interviewee not found", HttpStatus.NOT_FOUND),
    
    /**
     * Interview data provided is invalid or incomplete
     * <p>
     * Used when interview creation or update fails due to invalid or missing data.
     * </p>
     */
    INVALID_INTERVIEW_DATA(1504, "Invalid interview data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Interviewee type is invalid or not supported
     * <p>
     * Used when interviewee type is not one of the supported types in the system.
     * </p>
     */
    INVALID_INTERVIEWEE_TYPE(1505, "Invalid interviewee type", HttpStatus.BAD_REQUEST),
    
    /**
     * Interview creation failed due to scheduling conflicts
     * <p>
     * Used when interview creation fails due to scheduling conflicts or resource unavailability.
     * </p>
     */
    INTERVIEW_SCHEDULING_CONFLICT(1506, "Interview scheduling conflict", HttpStatus.CONFLICT),

    // ================= EVIDENCE MANAGEMENT (1600 - 1699) ===================
    
    /**
     * Evidence with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify evidence that cannot be found
     * in the database.
     * </p>
     */
    EVIDENCE_NOT_FOUND(1601, "Evidence not found", HttpStatus.NOT_FOUND),
    
    /**
     * Evidence creation failed due to invalid data
     * <p>
     * Used when evidence creation fails due to missing required fields or invalid data.
     * </p>
     */
    EVIDENCE_CREATION_FAILED(1602, "Evidence creation failed - invalid data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Evidence update failed due to invalid data or permissions
     * <p>
     * Used when evidence update operation fails due to invalid data or insufficient permissions.
     * </p>
     */
    EVIDENCE_UPDATE_FAILED(1603, "Evidence update failed", HttpStatus.BAD_REQUEST),
    
    /**
     * Evidence deletion failed due to constraints or permissions
     * <p>
     * Used when evidence deletion fails due to existing dependencies or insufficient permissions.
     * </p>
     */
    EVIDENCE_DELETE_FAILED(1604, "Evidence deletion failed", HttpStatus.BAD_REQUEST),

    // ================= INVESTIGATION PLAN (1700 - 1799) ===================
    
    /**
     * Investigation plan with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify an investigation plan that cannot be found
     * in the database.
     * </p>
     */
    INVESTIGATION_PLAN_NOT_FOUND(1701, "Investigation plan not found", HttpStatus.NOT_FOUND),
    
    /**
     * Investigation plan creation failed due to invalid data
     * <p>
     * Used when investigation plan creation fails due to missing required fields or invalid data.
     * </p>
     */
    INVESTIGATION_PLAN_CREATION_FAILED(1702, "Investigation plan creation failed - invalid data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Investigation plan update failed due to invalid data or permissions
     * <p>
     * Used when investigation plan update operation fails due to invalid data or insufficient permissions.
     * </p>
     */
    INVESTIGATION_PLAN_UPDATE_FAILED(1703, "Investigation plan update failed", HttpStatus.BAD_REQUEST),

    // ================= VALIDATION ERRORS (1800 - 1899) ===================
    
    /**
     * Invalid data format or structure provided
     * <p>
     * Used when request data has invalid format or structure that cannot be processed.
     * </p>
     */
    INVALID_DATA_FORMAT(1801, "Invalid data format provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid request body or missing required data
     * <p>
     * Used when request body is missing, null, or contains invalid structure.
     * </p>
     */
    INVALID_REQUEST_BODY(1802, "Invalid request body provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid content type for the request
     * <p>
     * Used when request content type is not supported by the endpoint.
     * </p>
     */
    INVALID_CONTENT_TYPE(1803, "Invalid content type - unsupported format", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid time range provided
     * <p>
     * Used when start time is after end time or time values are invalid.
     * </p>
     */
    INVALID_TIME_RANGE(1804, "Invalid time range - end time must be after start time", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid location data provided
     * <p>
     * Used when location field is missing or contains invalid data.
     * </p>
     */
    INVALID_LOCATION(1805, "Invalid location data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid parameters provided in request
     * <p>
     * Used when request parameters are missing, invalid, or out of range.
     * </p>
     */
    INVALID_PARAMETERS(1806, "Invalid parameters provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid level of trust value
     * <p>
     * Used when level of trust is not one of the accepted values.
     * </p>
     */
    INVALID_LEVEL_OF_TRUST(1807, "Invalid level of trust - must be 'a', 'b', or 'c'", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid question data provided
     * <p>
     * Used when question data is missing, null, or contains invalid structure.
     * </p>
     */
    INVALID_QUESTION_DATA(1808, "Invalid question data provided", HttpStatus.BAD_REQUEST),
    
    /**
     * Invalid file name provided
     * <p>
     * Used when file name is null, empty, or contains invalid characters.
     * </p>
     */
    INVALID_FILE_NAME(1809, "Invalid file name provided", HttpStatus.BAD_REQUEST),

    ;

    /**
     * Unique numeric error code
     */
    private final int code;
    
    /**
     * Human-readable error message
     */
    private final String message;
    
    /**
     * HTTP status code for REST API responses
     */
    private final HttpStatus statusCode;

    /**
     * Constructs an ErrorCode enum value
     * <p>
     * Creates an error code with specified numeric code, message, and HTTP status.
     * </p>
     *
     * @param code       Unique numeric identifier for the error
     * @param message    Human-readable error message
     * @param statusCode HTTP status code for REST API responses
     */
    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    /**
     * Retrieves the numeric error code
     * <p>
     * Returns the unique numeric identifier associated with this error code.
     * </p>
     *
     * @return The numeric error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Retrieves the error message
     * <p>
     * Returns the human-readable error message associated with this error code.
     * </p>
     *
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieves the HTTP status code
     * <p>
     * Returns the HTTP status code that should be used in REST API responses
     * for this error condition.
     * </p>
     *
     * @return The HTTP status code
     */
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
