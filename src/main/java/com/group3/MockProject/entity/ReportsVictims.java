package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports_victims")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportsVictims {
    @EmbeddedId
    private ReportsVictimsId id;

    @ManyToOne
    @MapsId("reportId")
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne
    @MapsId("victimId")
    @JoinColumn(name = "victim_id")
    private Victim victim;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
} 