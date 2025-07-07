package com.group3.MockProject.service.impl;


import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.RecordInfo;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.dto.response.RecordInfoResponseDto;
import com.group3.MockProject.dto.response.UserResponseDto;
import com.group3.MockProject.repository.CaseRepository;
import com.group3.MockProject.repository.EvidenceRepository;
import com.group3.MockProject.repository.RecordInfoRepository;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CaseServiceImpl class
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

@Service
public class CaseServiceImpl implements CaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordInfoRepository recordInfoRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private EvidenceRepository evidenceRepository;

    @Override
    public Page<UserResponseDto> getAssignedOfficers(String caseId, Pageable pageable) {
        Page<User> users = userRepository.findOfficersByCaseId(caseId, pageable);
        return users.map(user -> new UserResponseDto(
                user.getUsername(),
                user.getFullname(),
                user.getAvatarUrl(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole() != null ? user.getRole().getRoleId() : null
        ));
    }

    @Override
    public RecordInfoResponseDto createRecord(String caseId, CreateRecordInfoDto requestDto) {
        try {
            Case caseEntity = caseRepository.findById(caseId)
                    .orElseThrow(() -> new RuntimeException("Case not found: " + caseId));

            Evidence evidence = null;
//            if (requestDto.getEvidenceId() != null) {
//                evidence = evidenceRepository.findById(requestDto.getEvidenceId())
//                        .orElseThrow(() -> new RuntimeException("Evidence not found: " + requestDto.getEvidenceId()));
//            }else{
//                 evidence = new Evidence();
//                 evidence.setCaseEntity(caseEntity);
//                 evidenceRepository.save(evidence);
//            }

            RecordInfo record = new RecordInfo();
            record.setTypeName(requestDto.getTypeName());
            record.setSource(requestDto.getSource());
            record.setDateCollected(requestDto.getDateCollected() != null ? requestDto.getDateCollected() : LocalDateTime.now());
            record.setSummary(requestDto.getSummary());
            record.setDeleted(requestDto.getIsDeleted() != null ? requestDto.getIsDeleted() : false);
            record.setEvidence(evidence); // Set to null

            RecordInfo saved = recordInfoRepository.saveAndFlush(record);
            RecordInfoResponseDto responseDto = new RecordInfoResponseDto();
            responseDto.setRecordInfoId(saved.getRecordInfoId());
            responseDto.setTypeName(saved.getTypeName());
            responseDto.setSource(saved.getSource());
            responseDto.setDateCollected(saved.getDateCollected());
            responseDto.setSummary(saved.getSummary());
            responseDto.setIsDeleted(saved.isDeleted());
            responseDto.setEvidenceId(null);
            return responseDto;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error creating record: " + ex.getMessage(), ex);
        }
    }
}
