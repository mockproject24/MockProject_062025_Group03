package com.group3.MockProject.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

/**
 * ReportsWitnesses
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 03/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 03/07/2025      ASUS      Create
 */
@Entity
@Table(name = "reports_witnesses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportsWitnesses {

    @EmbeddedId
    ReportsWitnessesId id;

    @ManyToOne
    @MapsId("reportId")
    @JoinColumn(name = "report_id")
    Report report;

    @ManyToOne
    @MapsId("witnessId")
    @JoinColumn(name = "witness_id")
    Witness witness;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
