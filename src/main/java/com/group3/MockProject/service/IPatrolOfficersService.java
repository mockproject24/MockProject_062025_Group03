package com.group3.MockProject.service;

import com.group3.MockProject.dto.response.PaginatedResponse;
import com.group3.MockProject.dto.response.PatrolOfficersDto;
import org.springframework.data.domain.Pageable;

/**
 * IPatrolOfficersService
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
public interface IPatrolOfficersService {
    PaginatedResponse<PatrolOfficersDto> getAllPatrolOfficers(Pageable pageable);

    PaginatedResponse<PatrolOfficersDto> getAllPatrolOfficers(Pageable pageable, String search);
}
