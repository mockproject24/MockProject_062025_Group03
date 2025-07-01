package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "physical_invest")
public class PhysicalInvest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String evidenceId;

    @Column(name = "image_url",nullable = false)
    private String imageUrl;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

}
