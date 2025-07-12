package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.*;
import com.group3.MockProject.dto.response.MeasureSurvey;
import com.group3.MockProject.entity.*;
import org.springframework.stereotype.Service;

@Service
public class EvidentMapper {

    /**
     * Converts an Evidence entity to an EvidentDto.
     *
     * @param entity the Evidence entity to convert
     * @return the converted EvidentDto
     */
    public EvidentResponse toDto(Evidence entity) {
        if (entity == null) {
            return null;
        }
        return new EvidentResponse(
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

    /**
     * Converts a DigitalInvest entity to a DigitalInvestDto.
     *
     * @param entity the DigitalInvest entity to convert
     * @return the converted DigitalInvestDto
     */
    public DigitalInvestResponse toDto(DigitalInvest entity) {
        if (entity == null) {
            return null;
        }
        return new DigitalInvestResponse(
                entity.getDeviceType(),
                entity.getAnalystTool(),
                entity.getResult()
        );
    }

    /**
     * Converts a FinancialInvest entity to a FinancialInvestDto.
     *
     * @param entity the FinancialInvest entity to convert
     * @return the converted FinancialInvestDto
     */
    public FinancialInvestResponse toDto(FinancialInvest entity) {
        if (entity == null) {
            return null;
        }
        return new FinancialInvestResponse(
                entity.getSummary()
        );
    }

    /**
     * Converts a ForensicInvest entity to a ForensicInvestDto.
     *
     * @param entity the ForensicInvest entity to convert
     * @return the converted ForensicInvestDto
     */
    public ForensicInvestResponse toDto(ForensicInvest entity) {
        if (entity == null) {
            return null;
        }
        return new ForensicInvestResponse(
                entity.getLabName(),
                entity.getReport(),
                entity.getReceivedAt()
        );
    }

    /**
     * Converts a PhysicalInvest entity to a PhysicalInvestDto.
     *
     * @param entity the PhysicalInvest entity to convert
     * @return the converted PhysicalInvestDto
     */
    public PhysicalInvestResponse toDto(PhysicalInvest entity) {
        if (entity == null) {
            return null;
        }
        return new PhysicalInvestResponse(
                entity.getImageUrl()
        );
    }

    /**
     * Converts a MeasureSurvey entity to a MeasureSurveyDto.
     *
     * @param entity the MeasureSurvey entity to convert
     * @return the converted MeasureSurveyDto
     */
    public MeasureSurvey toDto(com.group3.MockProject.entity.MeasureSurvey entity) {
        if (entity == null) {
            return null;
        }
        return new MeasureSurvey(
                entity.getMeasureSurveyId(),
                entity.getTypeName(),
                entity.getSource(),
                entity.getResult()
        );
    }

    /**
     * Converts a RecordInfo entity to a RecordInfoDto.
     *
     * @param entity the RecordInfo entity to convert
     * @return the converted RecordInfoDto
     */
    public RecordInfoResponse toDto(RecordInfo entity) {
        if (entity == null) {
            return null;
        }
        return new RecordInfoResponse(
                entity.getRecordInfoId(),
                entity.getTypeName(),
                entity.getSource(),
                entity.getDateCollected(),
                entity.getSummary()
        );
    }

}
