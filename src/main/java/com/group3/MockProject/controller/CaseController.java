package com.group3.MockProject.controller;


import com.group3.MockProject.dto.request.CreateRecordInfoDto;
import com.group3.MockProject.dto.response.RecordInfoResponseDto;
import com.group3.MockProject.dto.response.ResponseDto;
import com.group3.MockProject.dto.response.UserResponseDto;
import com.group3.MockProject.entity.RecordInfo;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * CaseController
 *
 * Provides business logic for managing details.
 *
 * Version 1.0
 *
 * Date: 04/07/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

@RestController
@RequestMapping("/api/cases")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @GetMapping("/{caseId}/assigned-officers")
    public ResponseEntity<Map<String, Object>> getAssignedOfficers(
            @PathVariable String caseId,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<UserResponseDto> page = caseService.getAssignedOfficers(caseId, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("result", new HashMap<String, Object>() {{
            put("content", page.getContent());
            put("totalElements", page.getTotalElements());
            put("totalPages", page.getTotalPages());
            put("size", page.getSize());
            put("number", page.getNumber());
        }});
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{caseId}/records")
    public ResponseEntity<ResponseDto<RecordInfoResponseDto>> createRecord(
            @PathVariable String caseId,
            @RequestBody CreateRecordInfoDto requestDto) {
        try {
            RecordInfoResponseDto createdRecord = caseService.createRecord(caseId, requestDto);
            ResponseDto<RecordInfoResponseDto> response = new ResponseDto<>(201, "Record created successfully", createdRecord);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            ResponseDto<RecordInfoResponseDto> response = new ResponseDto<>(500, "Error creating record: " + e.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }
}
