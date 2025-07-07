package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasureSurveyDto {
    private String measureSurveyId;
    private String typeName;
    private String source;
    private String result;
}