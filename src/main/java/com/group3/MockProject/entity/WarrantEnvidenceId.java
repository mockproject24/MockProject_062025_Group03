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
public class WarrantEnvidenceId implements Serializable {
    @Column(name = "evidence_id")
    private String evidenceId;

    @Column(name = "suspect_id")
    private String suspectId;
}
