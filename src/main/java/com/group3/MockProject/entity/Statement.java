package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Statement
 * <p>
 * Represents a statement given by suspects, victims, or witnesses in a case
 * <p>
 * Version 1.0
 * Date: 7/23/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/23/2025      FongFox      Create
 */
@Entity
@Table(name = "statements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Statement {
    @Id
    @Column(name = "statement_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String statementId;

    @Column(name = "initial_name")
    String initialName;

    @Column(name = "statement_date")
    LocalDateTime statementDate;

    @Column(name = "contact_information")
    String contactInformation;

    @Column(name = "initial_role_type")
    String initialRoleType; // SUSPECT, VICTIM, WITNESS

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    String content;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "suspect_id")
    Suspect suspect;

    @ManyToOne
    @JoinColumn(name = "victim_id")
    Victim victim;

    @ManyToOne
    @JoinColumn(name = "witness_id")
    Witness witness;

    // Evidence links - many-to-many relationship
    @OneToMany(mappedBy = "statement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<StatementEvidence> statementEvidences;
}
