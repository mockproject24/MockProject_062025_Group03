package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

/**
 * StatementEvidence
 * <p>
 * Junction table for many-to-many relationship between Statement and Evidence
 * <p>
 * Version 1.0
 * Date: 7/23/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/23/2025      FongFox      Create
 */
@Entity
@Table(name = "statements_evidences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatementEvidence {
    @EmbeddedId
    StatementEvidenceId id;

    @ManyToOne
    @MapsId("statementId")
    @JoinColumn(name = "statement_id")
    Statement statement;

    @ManyToOne
    @MapsId("evidenceId")
    @JoinColumn(name = "evidence_id")
    Evidence evidence;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
