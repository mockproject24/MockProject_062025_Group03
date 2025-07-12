package com.group3.MockProject.mapper;

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
        return SuspectResponse.builder()
                .suspectId(suspect.getSuspectId())
                .fullName(suspect.getFullname())
                .address(suspect.getAddress())
                .mugshotUrl(suspect.getMugshotUrl())
                .caseId(suspect.getCaseEntity() != null ? suspect.getCaseEntity().getCaseId() : null)
                .build();
    }


}