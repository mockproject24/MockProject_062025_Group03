package com.group3.MockProject.exception;

/**
 * ResourceNotFoundException
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 06/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 06/07/2025    Hải Đăng      Create
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
