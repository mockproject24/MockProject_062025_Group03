package com.group3.MockProject.exception;

/**
 * AppException
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
public class AppException extends RuntimeException {
    
    /**
     * The error code associated with this exception
     */
    private ErrorCode errorCode;

    /**
     * Constructs a new AppException with the specified error code
     * <p>
     * Creates an exception instance using the error code's message and
     * associates the error code with the exception for later retrieval.
     * </p>
     *
     * @param errorCode The error code that describes the specific error condition
     */
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new AppException with the specified error code and custom message
     * <p>
     * Creates an exception instance using a custom message while still
     * associating the error code with the exception for later retrieval.
     * </p>
     *
     * @param errorCode The error code that describes the specific error condition
     * @param message   Custom error message to override the default error code message
     */
    public AppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new AppException with the specified error code and cause
     * <p>
     * Creates an exception instance using the error code's message and
     * includes the underlying cause of the exception.
     * </p>
     *
     * @param errorCode The error code that describes the specific error condition
     * @param cause     The underlying cause of this exception
     */
    public AppException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new AppException with the specified error code, custom message, and cause
     * <p>
     * Creates an exception instance using a custom message and includes
     * the underlying cause of the exception.
     * </p>
     *
     * @param errorCode The error code that describes the specific error condition
     * @param message   Custom error message to override the default error code message
     * @param cause     The underlying cause of this exception
     */
    public AppException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Retrieves the error code associated with this exception
     * <p>
     * Returns the ErrorCode enum value that was used to create this exception.
     * This can be used to determine the specific type of error and handle
     * it appropriately.
     * </p>
     *
     * @return The error code associated with this exception
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code for this exception
     * <p>
     * Updates the error code associated with this exception. This method
     * is typically used internally and should be used with caution.
     * </p>
     *
     * @param errorCode The new error code to associate with this exception
     */
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Returns a string representation of this exception
     * <p>
     * Provides a detailed string representation including the error code,
     * code number, and message for debugging purposes.
     * </p>
     *
     * @return A string representation of this exception
     */
    @Override
    public String toString() {
        return String.format("AppException{errorCode=%s, code=%d, message='%s'}", 
                           errorCode.name(), errorCode.getCode(), getMessage());
    }
}
