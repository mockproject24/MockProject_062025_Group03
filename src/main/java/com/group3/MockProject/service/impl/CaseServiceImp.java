package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.dto.response.DigitalInvestDto;
import com.group3.MockProject.dto.response.EvidentDto;
import com.group3.MockProject.dto.response.InvestigationDetailDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.exception.MockProjectException;
import com.group3.MockProject.mapper.CaseMapper;
import com.group3.MockProject.mapper.EvidentMapper;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidentRepository;
import com.group3.MockProject.service.ICaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaseServiceImp implements ICaseService {
    private final CaseRepository caseRepository;
    private final EvidentRepository evidentRepository;
    private final EvidentMapper mapper;
    private final CaseMapper caseMapper;


    @Override
    public List<EvidentDto<?>> getEvidences(String caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new MockProjectException("Case not found", 404));

        List<Evidence> evidences = evidentRepository.findByCaseEntity(caseEntity);

        List<EvidentDto<?>> evidentDtos = new ArrayList<>();

        for (Evidence evidence : evidences) {
            EvidentDto evidenceDto = mapper.toDto(evidence);
            if (evidence.getDigitalInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetailDto<DigitalInvestDto>(
                        "DigitalInvest",
                        mapper.toDto(evidence.getDigitalInvest())
                ));
            } else if (evidence.getFinancialInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetailDto<>(
                        "FinancialInvest",
                        mapper.toDto(evidence.getFinancialInvest())
                ));
            } else if (evidence.getForensicInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetailDto<>(
                        "ForensicInvest",
                        mapper.toDto(evidence.getForensicInvest())
                ));
            } else if (evidence.getPhysicalInvest() != null) {
                evidenceDto.setInvestigationDetail(new InvestigationDetailDto<>(
                        "PhysicalInvest",
                        mapper.toDto(evidence.getPhysicalInvest())
                ));
            }

            evidenceDto.setRecordInfo(evidence.getRecordInfos().stream().map(mapper::toDto).collect(Collectors.toSet()));
            evidenceDto.setMeasureSurvey(evidence.getMeasureSurveys().stream().map(mapper::toDto).collect(Collectors.toSet()));

            evidentDtos.add(evidenceDto);
        }

        return evidentDtos;
    }

    @Override
    public CaseListDto getListCase(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Case> cases;
        if(search == null || search.isEmpty())
            cases = caseRepository.findAll(pageable);
        else
            cases = caseRepository.findByCaseNumberContains(search, pageable);

        if (cases.isEmpty()) {
            return new CaseListDto(page, pageSize, 0, new ArrayList<>());
        }
        return caseMapper.toDto(cases);
    }
}
