package com.group3.MockProject.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * CaseListMetaResponse
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 12/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 12/07/2025      ASUS      Create
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseListMetaResponse {
    private List<MetaDto> caseTypes;
    private List<MetaDto> severities;
}
