package com.group3.MockProject.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Arrest class
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 01/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 01/07/2025        Bui Van Trang        Create
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class CaseResponseDto {
    private String caseId;
    private String caseNumber;
    private String typeCase;
    private String severity;
    private LocalDateTime createdAt;
    private String status;

    //From table User:fullname
    private String reporterFullname;

    //From table Report:case_location
    private String location;
}
