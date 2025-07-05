package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "reports_victims")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportsVictims {
    @EmbeddedId
    ReportsVictimsId id;

    @ManyToOne
    @MapsId("reportId")
    @JoinColumn(name = "report_id")
    Report report;

    @ManyToOne
    @MapsId("victimId")
    @JoinColumn(name = "victim_id")
    Victim victim;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
} 