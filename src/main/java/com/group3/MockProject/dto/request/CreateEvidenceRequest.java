package com.group3.MockProject.dto.request;

import com.group3.MockProject.constant.EvidenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * CreateEvidenceRequest
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/9/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/9/2025      NGUYEN NGOC SY      Create
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateEvidenceRequest {

    @NotBlank(message = "description must not be blank")
    String description;

    @NotBlank(message = "currentLocation must not be blank")
    String currentLocation;

    @NotNull(message = "evidenceType must not be null")
    EvidenceType evidenceType;

    LocalDateTime collectedAt;
}
