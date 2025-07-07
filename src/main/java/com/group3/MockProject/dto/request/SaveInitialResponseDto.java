package com.group3.MockProject.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SaveInitialResponseDto
 * <p>
 * DTO for saving initial response information
 * <p>
 * Version 1.0
 * Date: 04-Jul-25
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04-Jul-25     Hoang Tran     Create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveInitialResponseDto {

    @JsonProperty("dispatch_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dispatchTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("arrival_time")
    private LocalDateTime arrivalTime;

    private List<OfficerDto> officers;

    @JsonProperty("preliminary_assessment")
    private String preliminaryAssessment;

    @JsonProperty("preservation_measures")
    private List<PreservationMeasureDto> preservationMeasures;

    @JsonProperty("medical_support")
    private List<MedicalSupportDto> medicalSupport;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfficerDto {
        @JsonProperty("officer_id")
        private String officerId;
        private String responsible;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreservationMeasureDto {
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicalSupportDto {
        @JsonProperty("unit_id")
        private String unitId;

        @JsonProperty("type_of_support")
        private String typeOfSupport;

        @JsonProperty("time_of_arrival")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime timeOfArrival;
    }
} 