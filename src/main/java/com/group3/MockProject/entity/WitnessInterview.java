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
    @EmbeddedId
    private WitnessInterviewId id;

    @ManyToOne
    @JoinColumn(name = "witness_id")
    @MapsId("witnessId")
    private Witness witness;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    @MapsId("interviewId")
    private Interview interview;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
