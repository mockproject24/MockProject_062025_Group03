package com.group3.MockProject.entity;

/**
 * Role
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/2/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/2/2025      Ngoc Nghia      Create
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Role
 *
 * Provides business logic for managing employment details.
 *
 * Version 1.0
 * Date: 7/2/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/2/2025      Ngoc Nghia      Create
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column(name = "role_id")
    private String roleId;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @OneToMany(mappedBy = "role")
    private List<RolesPermissions> rolesPermissions;
}
