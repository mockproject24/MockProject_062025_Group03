package com.group3.MockProject.entity;

import jakarta.persistence.*;
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
    @EmbeddedId
    private SuspectEvidenceId id;

    @ManyToOne
    @JoinColumn(name = "evidence_id")
    @MapsId("suspectId")
    private Evidence evidence;

    @ManyToOne
    @JoinColumn(name = "suspect_id")
    @MapsId("suspectId")
    private Suspect suspect;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
