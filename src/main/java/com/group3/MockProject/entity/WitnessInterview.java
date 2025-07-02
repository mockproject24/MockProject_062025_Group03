package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "witnesses_interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WitnessInterview {
    @Id
    @Column(name = "interview_id")
    private String interviewId;

    @Id
    @Column(name = "witness_id")
    private String witnessId;

    @ManyToOne
    @JoinColumn(name = "witness_id", insertable = false, updatable = false)
    private Witness witness;

    @ManyToOne
    @JoinColumn(name = "interview_id", insertable = false, updatable = false)
    private Interview interview;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
