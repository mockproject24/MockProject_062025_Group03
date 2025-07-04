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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CaseController
 *
 * Provides controller for managing case information.
 *
 * Version 1.0
 * Date: 7/4/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      DQMinh      Create
 */
@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;
    private final SuspectMapper suspectMapper;
    @GetMapping("/{caseId}/suspects")
    public ApiResponse<?> getAllSuspects(@PathVariable("caseId") String caseId,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                         @RequestParam(value="status", required = false) String status,
                                         @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date){
        Pageable pageable = PageRequest.of(page-1, pageSize);

        Page<Suspect> suspectsPage = caseService.getAllSuspectsByCaseId(caseId, pageable, status, date);
        List<SuspectResponseDto> suspects = suspectsPage==null? new ArrayList<>():suspectsPage.getContent().stream().map(suspectMapper::toSuspectResponseDto).toList();
        Map<String,Object> responseResult = new HashMap<>();
        responseResult.put("suspects",suspects);
        responseResult.put("page",page);
        responseResult.put("pageSize",pageSize);
        responseResult.put("total",(suspectsPage==null? 0:suspectsPage.getTotalElements()));
        return ApiResponse.<Map<String,Object>>builder().status(HttpStatus.OK.value()).message("Get suspects successfully").result(responseResult).build();

    }

}
