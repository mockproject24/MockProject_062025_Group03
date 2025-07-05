package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "physicals_invests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhysicalInvest {
    @Id
    @Column(name = "evidence_id")
     String evidenceId;

    @Column(name = "image_url")
     String imageUrl;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
     boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "evidence_id")
     Evidence evidence;
}
