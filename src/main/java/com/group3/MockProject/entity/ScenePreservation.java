package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

/**
 * ScenePreservation
 * <p>
 * Entity for managing scene preservation information
 * <p>
 * Version 1.0
 * Date: 04-Jul-25
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04-Jul-25     Hoang Tran     Create
 */
@Entity
@Table(name = "scene_preservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenePreservation {

    @Id
    @Column(name = "preservation_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String preservationId;

    @ManyToOne
    @JoinColumn(name = "scene_response_id", nullable = false)
    SceneResponse sceneResponse;

    @Column(name = "officer_name")
    String officerName;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @Column(name = "protection_methods", columnDefinition = "TEXT")
    String protectionMethods;

    @Column(name = "area_covered")
    String areaCovered;

    @Column(name = "notes", columnDefinition = "TEXT")
    String notes;

    @Column(name = "attachment_url")
    String attachmentUrl;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
} 