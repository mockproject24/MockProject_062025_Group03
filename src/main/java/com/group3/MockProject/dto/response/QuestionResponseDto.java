package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private String questionId;
    private String interviewId;
    private String createdBy;
    private String content;
    private String answer;
    private Float reliability;
    private Boolean isDeleted;
} 