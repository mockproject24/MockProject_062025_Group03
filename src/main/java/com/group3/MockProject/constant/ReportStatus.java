package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ReportStatus
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
 * 07/07/2025     Hải Đăng      Create
 */
public enum ReportStatus {
    APPROVED("Approved"),
    PENDING("Pending"),
    REJECTED("Rejected");

    private final String label;

    ReportStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
