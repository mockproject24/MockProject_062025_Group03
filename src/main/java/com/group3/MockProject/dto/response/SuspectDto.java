package com.group3.MockProject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * SuspectDto
 * <p>
 *
 * <p>
 * Version 1.0
 * Date: 7/8/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/8/2025      DQMinh      Create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuspectDto {
    /**
     * Suspect unique identifier
     */
    private String suspectId;

    /**
     * Suspect full name
     */
    private String fullname;

    /**
     * Nationality
     */
    private String national;

    /**
     * Gender
     */
    private String gender;

    /**
     * Date of birth
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dob;

    /**
     * Identification number
     */
    private String identification;

    /**
     * Phone number
     */
    private String phoneNumber;

    /**
     * Description
     */
    private String description;

    /**
     * Address
     */
    private String address;

    /**
     * Catch time
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime catchTime;

    /**
     * Notes
     */
    private String notes;

    /**
     * Current status
     */
    private String status;

    /**
     * Mugshot URL
     */
    private String mugshotUrl;

    /**
     * Fingerprints hash
     */
    private String fingerprintsHash;

    /**
     * Health status
     */
    private String healthStatus;

    /**
     * Case ID associated with this suspect
     */
    private String caseId;

}
