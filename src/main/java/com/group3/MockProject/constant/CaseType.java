package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * CaseType
 *
 * Provides business logic for managing  details.
 *
 * Version 1.0
 *
 * Date: 08/07/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 08/07/2025        Nguyễn Bảo Kha        Create
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
