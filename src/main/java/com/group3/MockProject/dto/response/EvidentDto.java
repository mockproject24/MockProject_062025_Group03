package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvidentDto<T> {
    private String evidenceId;
    private String description;
    private LocalDateTime collectedAt;
    private String currentLocation;
    private String attachFile;
    private String status;
    private boolean isDeleted;
    private String caseId;
    private String userId;
    private String reportId;
    private String warrantId;
    private Set<RecordInfoDto> recordInfo;
    private Set<MeasureSurveyDto> measureSurvey;
    private InvestigationDetailDto<T> investigationDetail;
}