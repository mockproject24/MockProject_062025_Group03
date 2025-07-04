package com.group3.MockProject.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import java.io.Serializable;

/**
 * ReportsWitnessesId
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
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportsWitnessesId implements Serializable {

    @Column(name = "report_id")
    String reportId;

    @Column(name = "witness_id")
    String witnessId;
}