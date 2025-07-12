package com.group3.MockProject.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

@Getter
@RequiredArgsConstructor
public enum SeverityType {
    LOW("Low Severity"),
    MEDIUM("Medium Severity"),
    HIGH("High Severity"),
    CRITICAL("Critical Severity");

    private final String label;
}
