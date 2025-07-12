package com.group3.MockProject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode
 *
 * Provides business logic for managing employment details.
 *
 * Version 1.0
 * Date: 7/12/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/12/2025      Ngoc Nghia      Create
 */
@Getter
public enum ErrorCode {
    // ================= SYSTEM ERROR (9000 - 9999) =================
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(9001, "Invalid key provided.", HttpStatus.BAD_REQUEST),

    // ================= CASE (1000 - 1099) =================
    CASE_NOT_EXISTED(1001, "Case not existed", HttpStatus.NOT_FOUND),

    // ================= SUSPECT (1100 - 1199) =================
    SUSPECT_NOT_EXISTED(1101, "Suspect not existed", HttpStatus.NOT_FOUND),
    SUSPECT_INVALID_NAME(1102, "Suspect full name is required and must not be blank", HttpStatus.BAD_REQUEST),
    SUSPECT_INVALID_ADDRESS(1103, "Suspect address is required and must not be blank", HttpStatus.BAD_REQUEST),

    // ================= USER (1200 - 1299) =================
    USER_NOT_EXISTED(1201, "User not existed", HttpStatus.NOT_FOUND),
    USER_EXISTED(1202, "User existed", HttpStatus.CONFLICT),
    USER_UNAUTHENTICATED(1203, "Unauthenticated", HttpStatus.UNAUTHORIZED),

    // ================= FILE UPLOAD (1300 - 1399) =================
    FILE_EMPTY(1301, "File is empty. Please upload a file.", HttpStatus.BAD_REQUEST),
    FILE_INVALID_EXTENSION(1302, "Invalid file extension.", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1303, "File upload failed.", HttpStatus.INTERNAL_SERVER_ERROR),

    ;
    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
