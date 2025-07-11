package com.group3.MockProject.controller;

import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.response.PaginatedResponse;
import com.group3.MockProject.dto.response.PatrolOfficersDto;
import com.group3.MockProject.service.impl.PatrolOfficersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * PatrolOfficersController
 * <p>
 * Retrieves a paginated and filtered list of patrol officers for assignment to the scene.
 * <p>
 * Version 1.0
 * <p>
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE              AUTHOR                 DESCRIPTION
 * -------------------------------------------------------------
 * 10/07/2025        Nguyen Quoc Tri        Create
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/officers")
public class PatrolOfficersController {

    private final PatrolOfficersService patrolOfficersService;

    /**
     * Gets patrol officers.
     * Retrieves a paginated and filtered list of patrol officers for assignment to the scene.
     *
     * @param page the page
     * @param pageSize the pageSize
     * @param search keyword
     * @return the patrol officers
     */
    @GetMapping
    public ResponseEntity<ResponseDto<PaginatedResponse<PatrolOfficersDto>>> getPatrolOfficers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search
            ) {
        try {
            // Validate page parameters
            if (page < 1) page = 1;
            if (pageSize <= 0) pageSize = 10;
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            PaginatedResponse<PatrolOfficersDto> response = patrolOfficersService.getAllPatrolOfficers(pageable, search);
            return ResponseEntity.ok(
                    ResponseDto.success("Successfully retrieved patrol officers", response)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseDto.error("Failed to retrieve patrol officers: " + e.getMessage(), 400)
            );
        }
    }
}
