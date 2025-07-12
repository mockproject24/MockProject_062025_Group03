package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CaseListDto
 *
 * Provides response structure for case listing with pagination.
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
public class CaseListResponse {
    
    /**
     * Current page number
     */
    private Integer page;
    
    /**
     * Number of items per page
     */
    private Integer pageSize;
    
    /**
     * Total number of cases
     */
    private Long total;
    
    /**
     * List of cases data
     */
    private List<CaseResponse> data;
}