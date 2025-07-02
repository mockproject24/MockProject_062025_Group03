package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SuspectEvidence class
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 01/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 01/07/2025        Nguyễn Bảo Kha        Create
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "suspect_evidence")
public class SuspectEvidence {
    @Id
    private String evidenceId;
    private String suspectId;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
