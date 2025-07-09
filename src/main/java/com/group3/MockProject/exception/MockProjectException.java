package com.group3.MockProject.exception;

import lombok.Getter;

@Getter
public class MockProjectException extends RuntimeException {
    private final int status;

    public MockProjectException(String message, int status) {
        super(message);
        this.status = status;
    }
}
