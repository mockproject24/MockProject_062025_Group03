package com.group3.MockProject.exception;


import com.group3.MockProject.dto.ResponseDto;
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

}