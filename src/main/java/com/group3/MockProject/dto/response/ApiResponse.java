package com.group3.MockProject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse
 *
 * Provides standardized API response format for all endpoints.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 08-07-2025         Ngoc Nghia            Create
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * HTTP status code
     */
    private Integer code;

    /**
     * Response message
     */
    private String message;

    /**
     * Response data
     */
    private T result;

    /**
     * Creates a successful response
     * @param data The response data
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("Success")
                .result(data)
                .build();
    }

    /**
     * Creates a successful response with custom message
     * @param message Custom success message
     * @param data The response data
     * @return ApiResponse with success status
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .result(data)
                .build();
    }

    /**
     * Creates an error response
     * @param code Error code
     * @param message Error message
     * @return ApiResponse with error status
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .result(null)
                .build();
    }

    /**
     * Creates a bad request error response
     * @param message Error message
     * @return ApiResponse with 400 status
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return ApiResponse.<T>builder()
                .code(400)
                .message(message)
                .result(null)
                .build();
    }

    /**
     * Creates an internal server error response
     * @param message Error message
     * @return ApiResponse with 500 status
     */
    public static <T> ApiResponse<T> internalServerError(String message) {
        return ApiResponse.<T>builder()
                .code(500)
                .message(message)
                .result(null)
                .build();
    }
}