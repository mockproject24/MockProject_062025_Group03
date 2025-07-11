package com.group3.MockProject.exception;


import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MockProjectException.class)
    public ResponseEntity<ResponseDto<String>> handleMockProjectException(MockProjectException ex) {
        ResponseDto<String> response = ResponseDto.error(ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(StorageException.class)
    public ApiResponse<?> handleStorageException(StorageException ex) {
        return ApiResponse.<Void>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
    }

}