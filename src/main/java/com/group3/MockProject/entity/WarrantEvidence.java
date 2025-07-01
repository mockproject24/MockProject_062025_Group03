package com.group3.MockProject.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warrant_evident")
@Builder
@IdClass(WarrantEnvidenceId.class)
public class WarrantEvidence implements Serializable {
    @Id
    @Column(name = "warrant_id")
    String warrantId;

    @Id
    @Column(name = "evidence_id")
    String evidenceId;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;
}
