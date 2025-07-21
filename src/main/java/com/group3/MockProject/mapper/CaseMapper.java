package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.CaseResponse;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Report;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CaseMapper {
    public CaseResponse convertToCaseResponse(Case caseEntity) {
        return CaseResponse.builder()
                .caseId(caseEntity.getCaseId())
                .caseNumber("#" + caseEntity.getCaseId())
                .caseName(caseEntity.getCaseName())
                .typeCase(caseEntity.getTypeCase().getLabel())
                .typeCaseKey(caseEntity.getTypeCase().name())
                .severity(caseEntity.getSeverity().getLabel())
                .severityKey(caseEntity.getSeverity().name())
                .status(caseEntity.getStatus().getLabel())
                .statusKey(caseEntity.getStatus().name())
                .createdAt(caseEntity.getCreateAt())
                .receivingUnit("Police Department") // Default value
                .location("N/A") // Default value hoặc lấy từ reports
                .reporterFullname(getReporterFullname(caseEntity))
                .build();
    }

    private String getReporterFullname(Case caseEntity) {
        return caseEntity.getReports().stream()
                .findFirst()
                .map(Report::getReporterFullname)
                .orElse("Unknown");
    }
}
