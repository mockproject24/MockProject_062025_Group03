package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * CaseStatus
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 08/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 08/07/2025        Nguyễn Bảo Kha        Create
 */

public enum CaseStatus {
    PENDING_APPROVAL("Pending Approval"),
    IN_PROCESS("In Process"),
    DONE("Done");

    private final String label;

    CaseStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
