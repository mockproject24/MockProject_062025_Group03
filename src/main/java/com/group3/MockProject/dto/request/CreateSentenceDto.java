package com.group3.MockProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSentenceDto {
    private String sentenceId;
    private String caseId;
    private String caseResultId;
    private String sentenceType;
    private String duration;
    private String condition;
    private LocalDateTime sentencingDate;
} 