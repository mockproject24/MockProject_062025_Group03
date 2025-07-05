package com.group3.MockProject.controller;

import com.group3.MockProject.dto.request.ApiResponse;
import com.group3.MockProject.dto.request.PaginatedResponse;
import com.group3.MockProject.dto.response.CaseResponseDto;
import com.group3.MockProject.service.CaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cases")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<CaseResponseDto>>> getAllCases(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(caseService.getAllCase(page, pageSize));
    }
}
