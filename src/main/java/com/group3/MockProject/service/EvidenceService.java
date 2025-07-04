package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.UpdateEvidenceDto;
import com.group3.MockProject.dto.response.EvidenceResponseDto;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.mapper.EvidenceMapper;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.repository.ReportRepository;
import com.group3.MockProject.repository.WarrantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final ReportRepository reportRepository;
    private final WarrantRepository warrantRepository;
    private final EvidenceMapper evidenceMapper;

    /**
     * Update evidence by ID. Only updates fields provided (PATCH-like behavior).
     */
    public EvidenceResponseDto updateEvidence(String evidenceId, UpdateEvidenceDto request) {
        Evidence evidence = evidenceRepository.findById(evidenceId)
                .orElseThrow(() -> new RuntimeException("Evidence not found with id: " + evidenceId));

        if (request.getDescription() != null) {
            evidence.setDescription(request.getDescription());
        }
        if (request.getCollectedAt() != null) {
            evidence.setCollectedAt(request.getCollectedAt());
        }
        if (request.getCurrentLocation() != null) {
            evidence.setCurrentLocation(request.getCurrentLocation());
        }
        if (request.getAttachFile() != null) {
            evidence.setAttachFile(request.getAttachFile());
        }
        if (request.getStatus() != null) {
            evidence.setStatus(request.getStatus());
        }

        if (request.getReportId() != null) {
            evidence.setReport(
                    reportRepository.findById(request.getReportId())
                            .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + request.getReportId()))
            );
        }

        if (request.getWarrantId() != null) {
            evidence.setWarrant(
                    warrantRepository.findById(request.getWarrantId())
                            .orElseThrow(() -> new IllegalArgumentException("Warrant not found with id: " + request.getWarrantId()))
            );
        }

        Evidence saved = evidenceRepository.save(evidence);

        return evidenceMapper.toResponse(saved);
    }
}
