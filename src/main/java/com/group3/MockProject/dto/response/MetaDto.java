package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * MetaDto
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
public class MetaDto {
    private String key;
    private String label;
}
