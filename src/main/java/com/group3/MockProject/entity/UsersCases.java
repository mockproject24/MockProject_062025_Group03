package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersCases {
    @EmbeddedId
    UsersCasesId id;

    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    String notes;

    @Column(name = "assigned_at")
    LocalDateTime assignedAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username")
    User user;

    @ManyToOne
    @MapsId("caseId")
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "caseUser")
    Set<Task> tasks;
} 