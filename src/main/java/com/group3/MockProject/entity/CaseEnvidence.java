package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cases_evidences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseEnvidence {
    @EmbeddedId
    private CaseEvidenceId id;

    @ManyToOne
    @MapsId("caseId")
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne
    @MapsId("evidenceId")
    @JoinColumn(name = "evidence_id")
    private Envidency evidence;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
