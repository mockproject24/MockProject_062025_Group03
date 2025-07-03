package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PhysicalInvest class
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 01/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 01/07/2025        Nguyễn Bảo Kha        Create
 */

@Entity
@Table(name = "physical_invest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalInvest {
    @Id
    @Column(name = "evidence_id")
    private String evidenceId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "evidence_id")
    @MapsId
    private Envidency evidence;
}
