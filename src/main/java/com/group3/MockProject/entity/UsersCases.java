package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersCases {
    @EmbeddedId
    private UsersCasesId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("caseId")
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
} 