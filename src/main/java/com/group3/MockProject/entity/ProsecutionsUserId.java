package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * ProsecutionsUserId
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProsecutionsUserId implements Serializable {
    @Column(name = "user_id")
    String userId;
    @Column(name = "prosecution_id")
    String prosecutionId;
}
