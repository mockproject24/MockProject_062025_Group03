package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "witness")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Witness {
    @Id
    @Column(name = "witness_id")
    private String witnessId;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "contact")
    private String contact;

    @Column(name = "statement", columnDefinition = "TEXT")
    private String statement;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @OneToMany(mappedBy = "witness")
    private List<WitnessInterview> interviews;
}