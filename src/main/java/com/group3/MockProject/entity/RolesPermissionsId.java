package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermissionsId implements Serializable {
    @Column(name = "role_id")
    private String roleId;

    @Column(name = "permission_id")
    private String permissionId;
} 