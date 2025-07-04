package com.group3.MockProject.controller;

import com.group3.MockProject.dto.response.InvestigationPlanResponseDto;
import com.group3.MockProject.dto.response.ResponseDto;
import com.group3.MockProject.service.InvestigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InvestigationPlanController
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

@RestController
@RequestMapping("/api/investigations")
public class InvestigationPlanController {
    @Autowired
    private InvestigationService investigationService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getInvestigations(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<InvestigationPlanResponseDto> page = investigationService.getInvestigations(pageable);
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
}
