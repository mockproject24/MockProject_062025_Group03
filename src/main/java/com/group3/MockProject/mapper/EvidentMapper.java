package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.entity.*;
import org.springframework.stereotype.Service;

@Service
public class EvidentMapper {

    public EvidentDto toDto(Evidence entity) {
        if (entity == null) {
            return null;
        }
        return new EvidentDto(
                entity.getEvidenceId(),
                entity.getDescription(),
                entity.getCollectedAt(),
                entity.getCurrentLocation(),
                entity.getAttachFile(),
                entity.getStatus(),
                entity.isDeleted(),
                entity.getCaseEntity() != null ? entity.getCaseEntity().getCaseId() : null,
                entity.getUser() != null ? entity.getUser().getUsername() : null,
                entity.getReport() != null ? entity.getReport().getReportId() : null,
                entity.getWarrant() != null ? entity.getWarrant().getWarrantId() : null,
                null,
                null,
                null
        );
    }
    public DigitalInvestDto toDto(DigitalInvest entity) {
        if (entity == null) {
            return null;
        }
        return new DigitalInvestDto(
                entity.getDeviceType(),
                entity.getAnalystTool(),
                entity.getResult()
        );
    }
    public FinancialInvestDto toDto(FinancialInvest entity) {
        if (entity == null) {
            return null;
        }
        return new FinancialInvestDto(
                entity.getSummary()
        );
    }
    public ForensicInvestDto toDto(ForensicInvest entity) {
        if (entity == null) {
            return null;
        }
        return new ForensicInvestDto(
                entity.getLabName(),
                entity.getReport(),
                entity.getReceivedAt()
        );
    }
    public PhysicalInvestDto toDto(PhysicalInvest entity) {
        if (entity == null) {
            return null;
        }
        return new PhysicalInvestDto(
                entity.getImageUrl()
        );
    }
    public MeasureSurveyDto toDto(MeasureSurvey entity) {
        if (entity == null) {
            return null;
        }
        return new MeasureSurveyDto(
                entity.getMeasureSurveyId(),
                entity.getTypeName(),
                entity.getSource(),
                entity.getResult()
        );
    }
    public RecordInfoDto toDto(RecordInfo entity) {
        if (entity == null) {
            return null;
        }
        return new RecordInfoDto(
                entity.getRecordInfoId(),
                entity.getTypeName(),
                entity.getSource(),
                entity.getDateCollected(),
                entity.getSummary()
        );
    }

}