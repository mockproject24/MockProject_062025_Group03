package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDto {
    private String caseId;
    private String caseNumber;
    private String typeCase;
    private String severity;
    private String status;
    private String summary;
    private LocalDateTime createAt;
    private String location;
}