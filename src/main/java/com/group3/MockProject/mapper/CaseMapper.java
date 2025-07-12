package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.CaseDto;
import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.elasticsearch.document.EsCase;
import com.group3.MockProject.entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaseMapper {



    public CaseDto toDto(EsCase esCase) {
        if (esCase == null) {
            return null;
        }

        return CaseDto.builder()
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
