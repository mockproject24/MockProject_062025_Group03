package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * SuspectResponseDto
 *
 * Provides suspect data transfer object for API responses.
 *
 * Version 1.0
 *
 * Date: 08-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 08-07-2025         Group3            Create
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspectResponseDto {
    
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