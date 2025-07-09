package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.CaseDetailDto;
import com.group3.MockProject.dto.response.CaseDto;
import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaseMapper {

    /**
     * Converts a Case entity to a CaseDto.
     *
     * @param entity   the Case entity to convert
     * @param location the location associated with the case
     * @return the converted CaseDto
     */
    public CaseDto toDto(Case entity, String location) {
        if (entity == null) {
            return null;
        }
        return new CaseDto(
                entity.getCaseId(),
                entity.getCaseName(),
                entity.getTypeCase(),
                entity.getSeverity(),
                entity.getStatus(),
                entity.getCreateAt(),
                "Local PD – Investigation Division", // Default receiving unit
                location
        );
    }

    /**
     * Converts a paginated list of Case entities to a CaseListDto.
     *
     * @param casePage the paginated list of Case entities
     * @return the converted CaseListDto
     */
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

    /**
     * Converts Case entity to CaseDetailDto with all related collections
     *
     * @param caseEntity       The case entity to convert
     * @param suspectEntities  List of suspects for the case
     * @param warrantEntities  List of warrants for the case
     * @param evidenceEntities List of evidences for the case
     * @return CaseDetailDto containing complete case details
     */
    public CaseDetailDto toCaseDetailDto(Case caseEntity, List<Suspect> suspectEntities,
                                         List<Warrant> warrantEntities, List<Evidence> evidenceEntities) {
        List<CaseDetailDto.TaskDto> tasks = new ArrayList<>();
        if (caseEntity.getUsersCases() != null) {
            for (UsersCases userCase : caseEntity.getUsersCases()) {
                if (userCase.getTasks() != null && !userCase.isDeleted()) {
                    for (Task task : userCase.getTasks()) {
                        if (!task.isDeleted()) {
                            tasks.add(CaseDetailDto.TaskDto.builder()
                                    .taskId(task.getTaskId())
                                    .taskName(task.getTaskName())
                                    .content(task.getContent())
                                    .status(task.getStatus())
                                    .startDate(task.getStartDate())
                                    .dueDate(task.getDueDate())
                                    .completedAt(task.getCompletedAt())
                                    .build());
                        }
                    }
                }
            }
        }

        List<CaseDetailDto.SuspectDto> suspects = suspectEntities.stream()
                .map(suspect -> CaseDetailDto.SuspectDto.builder()
                        .suspectId(suspect.getSuspectId())
                        .fullname(suspect.getFullname())
                        .national(suspect.getNational())
                        .gender(suspect.getGender())
                        .dob(suspect.getDob())
                        .identification(suspect.getIdentification())
                        .phoneNumber(suspect.getPhoneNumber())
                        .description(suspect.getDescription())
                        .address(suspect.getAddress())
                        .catchTime(suspect.getCatchTime())
                        .notes(suspect.getNotes())
                        .status(suspect.getStatus())
                        .mugshotUrl(suspect.getMugshotUrl())
                        .fingerprintsHash(suspect.getFingerprintsHash())
                        .healthStatus(suspect.getHealthStatus())
                        .build())
                .toList();

        List<CaseDetailDto.WarrantDto> warrants = warrantEntities.stream()
                .map(warrant -> CaseDetailDto.WarrantDto.builder()
                        .warrantId(warrant.getWarrantId())
                        .warrantName(warrant.getWarrantName())
                        .attachedFile(warrant.getAttachedFile())
                        .timePublish(warrant.getTimePublish())
                        .deadline(warrant.getDeadline())
                        .status(warrant.getStatus())
                        .build())
                .toList();

        List<CaseDetailDto.EvidenceDto> evidences = evidenceEntities.stream()
                .map(evidence -> CaseDetailDto.EvidenceDto.builder()
                        .evidenceId(evidence.getEvidenceId())
                        .description(evidence.getDescription())
                        .collectedAt(evidence.getCollectedAt())
                        .currentLocation(evidence.getCurrentLocation())
                        .attachFile(evidence.getAttachFile())
                        .status(evidence.getStatus())
                        .evidenceType(evidence.getEvidenceType())
                        .build())
                .toList();

        return CaseDetailDto.builder()
                .caseId(caseEntity.getCaseId())
                .caseName(caseEntity.getCaseName())
                .typeCase(caseEntity.getTypeCase())
                .severity(caseEntity.getSeverity())
                .status(caseEntity.getStatus())
                .summary(caseEntity.getSummary())
                .createAt(caseEntity.getCreateAt())
                .tasks(tasks)
                .suspects(suspects)
                .warrants(warrants)
                .evidences(evidences)
                .build();
    }
}
