package com.group3.MockProject.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * RecordInfoResponseDto class
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
public class RecordInfoResponseDto {
    private String recordInfoId;
    private String typeName;
    private String source;
    private LocalDateTime dateCollected;
    private String summary;
    private Boolean isDeleted;
    private String evidenceId;
}

