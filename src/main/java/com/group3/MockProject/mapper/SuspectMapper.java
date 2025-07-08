package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.SuspectResponseDto;
import com.group3.MockProject.entity.Suspect;
import org.springframework.stereotype.Component;

/**
 * SuspectMapper
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      doanm      Create
 */
@Component
public class SuspectMapper {
    public SuspectResponseDto toSuspectResponseDto(Suspect suspect) {
        SuspectResponseDto suspectResponseDto = new SuspectResponseDto();
        suspectResponseDto.setSuspectId(suspect.getSuspectId());
        suspectResponseDto.setFullname(suspect.getFullname());
        suspectResponseDto.setStatus(suspect.getStatus());
        return suspectResponseDto;
    }
}
