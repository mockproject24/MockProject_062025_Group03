package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "cases_evidences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CaseEvidence {
    @EmbeddedId
    CaseEvidenceId id;

    @ManyToOne
    @MapsId("caseId")
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @ManyToOne
    @MapsId("evidenceId")
    @JoinColumn(name = "evidence_id")
    Evidence evidence;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
