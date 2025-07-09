package com.group3.MockProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionDto {
    private String permissionId;
    private String description;
    private Boolean isDeleted;
} 