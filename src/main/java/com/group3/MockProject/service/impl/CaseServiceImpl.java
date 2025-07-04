package com.group3.MockProject.service.impl;

import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.repository.SuspectRepository;
import com.group3.MockProject.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * CaseServiceImpl
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
 * 7/4/2025      DQMinh      Create
 */
@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final SuspectRepository suspectRepository;
    @Override
    public Page<Suspect> getAllSuspectsByCaseId(String caseId, Pageable pageable, String status, LocalDate date) {
        if(date != null){
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            return suspectRepository.findByCaseIdAndStatusAndCatchTime(caseId, status, date, startOfDay, endOfDay, pageable );
        }
        return suspectRepository.findByCaseIdAndStatusAndCatchTime(caseId, status, date, null, null, pageable );
    }
}
