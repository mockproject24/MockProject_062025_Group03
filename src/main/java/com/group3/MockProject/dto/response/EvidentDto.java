package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<RecordInfoDto> recordInfo;
    private List<MeasureSurveyDto> measureSurvey;
    private InvestigationDetailDto<T> investigationDetail;
}