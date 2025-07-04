package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.CaseDto;
import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaseMapper {

    public CaseDto toDto(Case entity, String location) {
        if (entity == null) {
            return null;
        }
        return new CaseDto(
                entity.getCaseId(),
                entity.getCaseNumber(),
                entity.getTypeCase(),
                entity.getSeverity(),
                entity.getStatus(),
                entity.getSummary(),
                entity.getCreateAt(),
                location
        );
    }
    public CaseListDto toDto(Page<Case> casePage) {
        if (casePage == null) {
            return null;
        }

        List<CaseDto> caseDtos = casePage.getContent()
                .stream()
                .map(item -> item.getReports().isEmpty() ?
                        toDto(item, null) :
                        toDto(item, item.getReports().get(0).getCaseLocation()))
                .collect(Collectors.toList());

        return new CaseListDto(
                casePage.getNumber() + 1,
                casePage.getSize(),
                casePage.getTotalElements(),
                caseDtos
        );
    }

}
