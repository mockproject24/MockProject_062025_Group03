package com.group3.MockProject.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * CreateRecordInfo
 *
 * Provides business logic for managing details.
 *
 * Version 1.0
 *
 * Date: 04/07/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

@Data
public class CreateRecordInfoDto {
    private String typeName;
    private String source;
    private LocalDateTime dateCollected;
    private String summary;
    private Boolean isDeleted = false;
    private String evidenceId;
}
