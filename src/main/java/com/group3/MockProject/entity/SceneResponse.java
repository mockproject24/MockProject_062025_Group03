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
 * SceneResponse
 * <p>
 * Entity for managing scene response information
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
@Table(name = "scene_response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneResponse {

    @Id
    @Column(name = "scene_response_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String sceneResponseId;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    Case caseEntity;

    @Column(name = "dispatch_time")
    LocalDateTime dispatchTime;

    @Column(name = "arrival_time")
    LocalDateTime arrivalTime;

    @Column(name = "preliminary_assessment", columnDefinition = "TEXT")
    String preliminaryAssessment;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToMany(mappedBy = "sceneResponse")
    List<ScenePreservation> scenePreservations;

    @OneToMany(mappedBy = "sceneResponse")
    List<MedicalSupport> medicalSupports;
} 