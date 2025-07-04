package com.group3.MockProject.dto.request;


/**
 * UpdateEvidenceDto
 *
 * Provides business logic for managing employment details.
 *
 * Version 1.0
 * Date: 7/4/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      DBD      Create
 */
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateEvidenceDto {

    private String description;

    private LocalDateTime collectedAt;

    private String currentLocation;

    private String attachFile;

    private String status;

    private String reportId;

    private String warrantId;
}
