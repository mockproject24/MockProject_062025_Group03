package com.group3.MockProject.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * CreateSuspectResponse
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04/07/2025   Hải Đăng      Create
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuspectResponse {
    private String caseId;
    private String suspectId;
    private String fullName;
    private String address;
    private String moreInfo;
    private String mugshotUrl;
    private Instant uploadedAt;
}
