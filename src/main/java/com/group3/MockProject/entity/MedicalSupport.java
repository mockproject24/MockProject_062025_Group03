package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

/**
 * MedicalSupport
 * <p>
 * Entity for managing medical support information
 * <p>
 * Version 1.0
 * Date: 04-Jul-25
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04-Jul-25     Hoang Tran      Create
 */
@Entity
@Table(name = "medical_support")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalSupport {

    @Id
    @Column(name = "support_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String supportId;

    @ManyToOne
    @JoinColumn(name = "scene_response_id", nullable = false)
    SceneResponse sceneResponse;

    @Column(name = "unit_id")
    String unitId;

    @Column(name = "type_of_support")
    String typeOfSupport;

    @Column(name = "personnel_assigned")
    String personnelAssigned;

    @Column(name = "location_assigned")
    String locationAssigned;

    @Column(name = "scene_sketch_url")
    String sceneSketchUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    String notes;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
} 