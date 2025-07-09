package com.group3.MockProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArrestDto {
    private String officerId;
    private String suspectMirandaSignature;
    private LocalDateTime arrestStartTime;
    private LocalDateTime arrestEndTime;
    private Boolean isDeleted;
} 