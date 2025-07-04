package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "witnesses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Witness {
    @Id
    @Column(name = "witness_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String witnessId;

    @Column(name = "fullname")
    String fullname;

    @Column(name = "contact")
    String contact;

    @Column(name = "national")
    String national;

    @Column(name = "statement", columnDefinition = "MEDIUMTEXT")
    String statement;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "witness")
    List<WitnessInterview> interviews;

    @OneToMany(mappedBy = "witness")
    List<ReportsWitnesses> reportsWitnesses;
}