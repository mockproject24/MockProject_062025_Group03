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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evidences_suspects")
@Builder
@IdClass(WarrantEnvidenceId.class)
public class WarrantEvidence implements Serializable {
    @Id
    @Column(name = "evidence_id")
    private String evidenceId;

    @Id
    @Column(name = "suspect_id")
    private String suspectId;

    @ManyToOne
    @JoinColumn(name = "evidence_id", insertable = false, updatable = false)
    private Envidency evidence;

    @ManyToOne
    @JoinColumn(name = "suspect_id", insertable = false, updatable = false)
    private Suspect suspect;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
