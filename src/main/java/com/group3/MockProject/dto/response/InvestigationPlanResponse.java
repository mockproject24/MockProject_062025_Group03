package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationPlanResponse {
    private String investigationPlanId;
    private String caseId;
    private String typeOfCrime;
    private String levelSeverity;
    private LocalDateTime date;
    private String reporter;
    private String location;
    private String status;
} 