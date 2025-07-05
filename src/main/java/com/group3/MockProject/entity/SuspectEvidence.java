package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "suspects_evidences")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuspectEvidence {
    @EmbeddedId
    SuspectEvidenceId id;

    @ManyToOne
    @JoinColumn(name = "evidence_id")
    @MapsId("evidenceId")
    Evidence evidence;

    @ManyToOne
    @JoinColumn(name = "suspect_id")
    @MapsId("suspectId")
    Suspect suspect;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
