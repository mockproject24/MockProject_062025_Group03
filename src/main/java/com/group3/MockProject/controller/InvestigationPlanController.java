package com.group3.MockProject.controller;

import com.group3.MockProject.dto.request.CreateInvestigationPlanRequest;
import com.group3.MockProject.dto.response.ApiResponse;
import com.group3.MockProject.dto.response.CreateInvestigationRespone;
import com.group3.MockProject.dto.response.InvestigationPlanResponse;
import com.group3.MockProject.service.InvestigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
 * DATE              AUTHOR                DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

@RestController
@RequestMapping("/api/investigations")
@RequiredArgsConstructor
public class InvestigationPlanController {
    private final InvestigationService investigationService;

    @GetMapping
    public ApiResponse<Map<String, Object>> getInvestigations(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort) {

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<InvestigationPlanResponse> pageResult = investigationService.getInvestigations(pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("content", pageResult.getContent());
        result.put("totalElements", pageResult.getTotalElements());
        result.put("totalPages", pageResult.getTotalPages());
        result.put("size", pageResult.getSize());
        result.put("number", pageResult.getNumber());

        return ApiResponse.success("Success", result);
    }


    @PostMapping("/cases/{caseId}/investigations")
    public ResponseEntity<ApiResponse<CreateInvestigationRespone>> createInvestigationPlan(
            @ModelAttribute CreateInvestigationPlanRequest requestDto,
            @PathVariable Long caseId){
        CreateInvestigationRespone response = new CreateInvestigationRespone();
        ApiResponse<CreateInvestigationRespone> createInvestigationResponse = null;
        return ResponseEntity.ok(createInvestigationResponse);
    }
}
