package com.group3.MockProject.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuspectResponse {
    String caseId;
    String fullName;
    String address;
    String moreInfo;
    String mugshotUrl;
    Instant uploadedAt;
}
