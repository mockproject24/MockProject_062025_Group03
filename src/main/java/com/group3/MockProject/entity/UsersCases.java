package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "users_cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersCases {
    @EmbeddedId
    UsersCasesId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("caseId")
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @Column(name = "responsible", columnDefinition = "TEXT")
    String responsible;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
} 