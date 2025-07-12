package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * SeverityType
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 07/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 07/07/2025    Hải Đăng      Create
 */

public enum SeverityType {
    MINOR("Minor"),
    MODERATE("Moderate"),
    SERIOUS("Serious"),
    CRITICAL("Critical");

    private final String label;

    SeverityType(String label) {
        this.label = label;
    }

//    @JsonValue
    public String getLabel() {
        return label;
    }
}
