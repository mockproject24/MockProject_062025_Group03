package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Arrest class
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
@Entity
@Table(name = "arrests")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Arrest {
    @EmbeddedId
    ArrestId id;

    @ManyToOne
    @MapsId("caseId")
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @ManyToOne
    @MapsId("suspectId")
    @JoinColumn(name = "suspect_id")
    Suspect suspect;

    @Column(name = "suspect_miranda_signature")
    String suspectMirandaSignature;

    @Column(name = "arrest_start_time", nullable = false)
    LocalDateTime arrestStartTime;

    @Column(name = "arrest_end_time")
    LocalDateTime arrestEndTime;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
