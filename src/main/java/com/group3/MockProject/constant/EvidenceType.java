package com.group3.MockProject.constant;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * EvidenceType
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
public enum EvidenceType {
    PHYSICAL_EVIDENCE("Physical Evidence"),
    BIOLOGICAL_EVIDENCE("Biological Evidence"),
    TRACE_EVIDENCE("Trace Evidence"),
    DOCUMENTARY_EVIDENCE("Documentary Evidence"),
    DIGITAL_EVIDENCE("Digital Evidence");

    private final String label;

    EvidenceType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
