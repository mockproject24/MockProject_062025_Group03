package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.SuspectDto;
import com.group3.MockProject.dto.response.SuspectResponse;
import org.springframework.stereotype.Component;

import com.group3.MockProject.entity.Suspect;

/**
 * SuspectMapper
 *
 * Provides mapping functionality between Suspect entity and DTOs.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 08-07-2025         Ngoc Nghia            Create
 */
@Component
public class SuspectMapper {

    /**
     * Converts Suspect entity to SuspectResponseDto
     * @param suspect The suspect entity to convert
     * @return SuspectResponseDto containing suspect data
     */
    public SuspectResponse toSuspectResponse(Suspect suspect) {
        if (suspect == null) {
            return null;
        }
        return SuspectDto.builder()
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
                .caseId(suspect.getCaseEntity() != null ? suspect.getCaseEntity().getCaseId() : null)
                .build();
    }

    /**
     * Converts SuspectResponseDto to Suspect entity
     * @param suspectDto The suspect DTO to convert
     * @return Suspect entity
     */
    public Suspect toSuspectEntity(SuspectDto suspectDto) {
        if (suspectDto == null) {
            return null;
        }

        return Suspect.builder()
                .suspectId(suspectDto.getSuspectId())
                .fullname(suspectDto.getFullname())
                .national(suspectDto.getNational())
                .gender(suspectDto.getGender())
                .dob(suspectDto.getDob())
                .identification(suspectDto.getIdentification())
                .phoneNumber(suspectDto.getPhoneNumber())
                .description(suspectDto.getDescription())
                .address(suspectDto.getAddress())
                .catchTime(suspectDto.getCatchTime())
                .notes(suspectDto.getNotes())
                .status(suspectDto.getStatus())
                .mugshotUrl(suspectDto.getMugshotUrl())
                .fingerprintsHash(suspectDto.getFingerprintsHash())
                .healthStatus(suspectDto.getHealthStatus())
                .build();
    }
}