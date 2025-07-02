package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationPlanResponseDto {
    private String investigationPlanId;
    private String createdOfficerId;
    private String caseId;
    private LocalDateTime deadlineDate;
    private String result;
    private String status;
    private LocalDateTime createAt;
    private String planContent;
    private Boolean isDeleted;
} 