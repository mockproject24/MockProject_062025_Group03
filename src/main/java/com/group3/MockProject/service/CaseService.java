package com.group3.MockProject.service;

import com.group3.MockProject.entity.Suspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CaseService {
    Page<Suspect> getAllSuspectsByCaseId(String caseId, Pageable pageable, String status, LocalDate date);
}
