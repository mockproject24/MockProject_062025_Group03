package com.group3.MockProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
public class User {
    @Id
    private String username;

    @NotNull
    private String passwordHash;

    @NotNull
    private String fullname;

    private String avatarUrl;

    private String email;
    @NotNull
    private String phonenumber;

    @NotNull
    private LocalDateTime create_at;

    @NotNull
    @ColumnDefault("false")
    private Boolean is_deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", referencedColumnName = "role_id")
    private Role role;
}
