package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OfficerCaseDetailListDto
 *
 * Provides paginated response structure for officer case details listing.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 08-07-2025         Group3            Create
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficerCaseDetailListResponse {
    
    /**
     * Current page number
     */
    private Integer page;
    
    /**
     * Number of items per page
     */
    private Integer pageSize;
    
    /**
     * Total number of officers
     */
    private Long total;
    
    /**
     * Total number of pages
     */
    private Integer totalPages;
    
    /**
     * List of officer case details
     */
    private List<OfficerCaseDetailResponse> officers;
} 