package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordInfoDto {
    private String recordInfoId;
    private String typeName;
    private String source;
    private LocalDateTime dateCollected;
    private String summary;
}