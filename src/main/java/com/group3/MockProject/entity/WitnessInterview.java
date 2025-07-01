package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(WitnessInterviewId.class)
@Table(name = "witness_interview")
public class WitnessInterview {
    @Id
    @Column(name = "witness_id", nullable = false)
    private String witnessId;

    @Id
    @Column(name = "interview_id", nullable = false)
    private String interviewId;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "witness_id")
//    private Witness witness;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "interview_id")
//    private Interview interview;
}
