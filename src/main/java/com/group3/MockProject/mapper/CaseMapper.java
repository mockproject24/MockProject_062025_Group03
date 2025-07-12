package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.CaseResponse;
import com.group3.MockProject.elasticsearch.document.EsCase;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CaseMapper {



    public CaseResponse toDto(EsCase esCase) {
        if (esCase == null) {
            return null;
        }

        return CaseResponse.builder()
                .caseId(esCase.getCaseId())
                .typeCase(esCase.getTypeCaseLabel())
                .typeCaseKey(esCase.getTypeCaseKey())
                .severity(esCase.getSeverityLabel())
                .severityKey(esCase.getSeverityKey())
                .status(esCase.getStatusLabel())
                .statusKey(esCase.getStatusKey())
                .createdAt(esCase.getCreateAt() != null ?
                        LocalDateTime.parse(esCase.getCreateAt()) : null)
                .location(esCase.getCaseLocation())
                .reporterFullname(esCase.getReporterFullname())
                .build();
    }


}
