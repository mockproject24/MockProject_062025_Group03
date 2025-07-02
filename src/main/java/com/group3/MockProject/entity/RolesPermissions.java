package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermissions {
    @EmbeddedId
    private RolesPermissionsId id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
} 