package com.group3.MockProject.controller;

import com.group3.MockProject.dto.response.ApiResponse;
import com.group3.MockProject.dto.response.SuspectResponseDto;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.mapper.SuspectMapper;
import com.group3.MockProject.repository.SuspectRepository;
import com.group3.MockProject.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CaseController
 *
 * Provides controller for managing case information.
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
 * 04/07/2025        Nguyễn Bảo Kha, DQMinh        Create
 */

@RestController
@AllArgsConstructor
@RequestMapping("/api/cases")
public class CaseController {
    @Autowired
    private CaseService caseService;


    @GetMapping("/{caseId}")
    public ResponseEntity<Case> getCaseById(@PathVariable String caseId) {
        Case foundCase = caseService.getCaseById(caseId);
        return ResponseEntity.ok(foundCase);
    }
    private final CaseService caseService;
    private final SuspectMapper suspectMapper;
    @GetMapping("/{caseId}/suspects")
    public ApiResponse<?> getAllSuspects(@PathVariable("caseId") String caseId,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                         @RequestParam(value="status", required = false) String status,
                                         @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date){
        Pageable pageable = PageRequest.of(page-1, pageSize);

    @GetMapping("/{caseId}/assigned-officers")
    public ResponseEntity<Map<String, Object>> getAssignedOfficers(
            @PathVariable String caseId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDto> pageResult = caseService.getAssignedOfficers(caseId, pageable);
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
        Page<Suspect> suspectsPage = caseService.getAllSuspectsByCaseId(caseId, pageable, status, date);
        List<SuspectResponseDto> suspects = suspectsPage==null? new ArrayList<>():suspectsPage.getContent().stream().map(suspectMapper::toSuspectResponseDto).toList();
        Map<String,Object> responseResult = new HashMap<>();
        responseResult.put("suspects",suspects);
        responseResult.put("page",page);
        responseResult.put("pageSize",pageSize);
        responseResult.put("total",(suspectsPage==null? 0:suspectsPage.getTotalElements()));
        return ApiResponse.<Map<String,Object>>builder().status(HttpStatus.OK.value()).message("Get suspects successfully").result(responseResult).build();

    @GetMapping("")
    public ResponseEntity<ResponseDto<CaseListDto>> getCaseLists(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(required = false) String search){
        if(page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest().body(ResponseDto.error("Page and pageSize must be greater than 0"));
        }
        CaseListDto caseListDtos = caseService.getListCase(page, pageSize, search);
        return ResponseEntity.ok(ResponseDto.success(caseListDtos));
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
    @GetMapping("{caseId}/evidences")
    public ResponseEntity<ResponseDto<List<EvidentDto<?>>>> getEvidences(@PathVariable String caseId) {
        List<EvidentDto<?>> evidences = caseService.getEvidences(caseId);
        if (evidences.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ResponseDto.success(evidences));
    }
}
