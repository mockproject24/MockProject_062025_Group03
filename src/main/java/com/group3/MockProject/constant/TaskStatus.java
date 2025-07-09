package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TaskStatus
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 08/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 08/07/2025      ASUS      Create
 */
public enum TaskStatus {
    WAITING_EXECUTING("Waiting executing"),
    EXECUTING("Executing"),
    COMPLETED("Completed");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
