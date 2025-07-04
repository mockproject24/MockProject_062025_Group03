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

@Entity
@Table(name = "prosecutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prosecution {
    @Id
    @Column(name = "prosecution_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String prosecutionId;

    @Column(name = "decision")
    String decision;

    @Column(name = "decision_date")
    LocalDateTime decisionDate;

    @Column(name = "reason")
    String reason;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "prosecution")
    List<Indictment> indictments;

    @OneToMany(mappedBy = "prosecution")
    List<ProsecutionsUser> prosecutionsUsers;
}