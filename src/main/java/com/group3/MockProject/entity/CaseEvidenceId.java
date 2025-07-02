package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseEvidenceId implements Serializable {
    @Column(name = "case_id")
    private String caseId;

    @Column(name = "evidence_id")
    private String evidenceId;
}
