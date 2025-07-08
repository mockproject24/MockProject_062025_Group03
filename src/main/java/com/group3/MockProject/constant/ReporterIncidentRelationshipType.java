package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ReporterIncidentRelationshipType
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
 * 07/07/2025   Hải Đăng      Create
 */
public enum ReporterIncidentRelationshipType {
    VICTIM("Victim"),
    WITNESS("Witness"),
    BYSTANDER("Bystander");

    private final String label;

    ReporterIncidentRelationshipType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

}
