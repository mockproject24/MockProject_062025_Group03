package com.group3.MockProject.controller;

import com.group3.MockProject.dto.response.ErrorMessageResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * GlobalExceptionController
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/5/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/5/2025      User      Create
 */
@RestControllerAdvice
public class GlobalExceptionController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorMessageResponse> handleNotFound(
            EntityNotFoundException ex, WebRequest request) {
        ErrorMessageResponse err = new ErrorMessageResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorMessageResponse> handleBadRequest(
            BadRequestException ex, WebRequest request) {
        ErrorMessageResponse err = new ErrorMessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({DataIntegrityViolationException.class, EntityExistsException.class})
    public ResponseEntity<ErrorMessageResponse> handleConflict(
            BadRequestException ex, WebRequest request) {
        ErrorMessageResponse err = new ErrorMessageResponse(
                HttpStatus.CONFLICT.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorMessageResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        ErrorMessageResponse err = new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
