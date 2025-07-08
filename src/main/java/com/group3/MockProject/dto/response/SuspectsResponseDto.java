package com.group3.MockProject.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * SuspectResponseDto
 * Version 1.0
 * Copyright
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuspectsResponseDto {
    Integer page;
    Integer pageSize;
    Long total;
    Integer totalPages;
    List<SuspectDto> suspects;
}