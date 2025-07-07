package com.group3.MockProject.controller;

import com.group3.MockProject.dto.response.InvestigationPlanResponseDto;
import com.group3.MockProject.dto.response.ResponseDto;
import com.group3.MockProject.service.InvestigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<InvestigationPlanResponseDto> pageResult = investigationService.getInvestigations(pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("result", new HashMap<String, Object>() {{
            put("content", pageResult.getContent());
            put("totalElements", pageResult.getTotalElements());
            put("totalPages", pageResult.getTotalPages());
            put("size", pageResult.getSize());
            put("number", pageResult.getNumber());
        }});
        return ResponseEntity.ok(response);
    }
}
