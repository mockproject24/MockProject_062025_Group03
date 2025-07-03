package com.group3.MockProject.entity;

/**
 * SuspectEvidenceId
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/3/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/3/2025      Ngoc Nghia      Create
 */

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SuspectEvidenceId
 *
 * Provides business logic for managing employment details.
 *
 * Version 1.0
 * Date: 7/3/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/3/2025      Ngoc Nghia      Create
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuspectEvidenceId {
    @Column(name="evidence_id")
    private String evidenceId;
    @Column(name="suspect_id")
    private String suspectId;
}
