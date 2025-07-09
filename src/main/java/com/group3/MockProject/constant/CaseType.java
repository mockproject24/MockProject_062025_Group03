package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * CaseType
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
public enum CaseType {
    ROBBERY("Robbery"),
    MURDER("Murder"),
    RAPE("Rape");

    private final String label;

    CaseType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

}
