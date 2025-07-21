package com.group3.MockProject.dto.response;

public interface InvestigationPlanProjection {
    String getInvestigationPlanId();
    String getCaseId();
    String getTypeOfCrime();
    String getLevelSeverity();
    java.time.LocalDateTime getDate();
    String getReporter();
    String getLocation();
    String getStatus();
}
