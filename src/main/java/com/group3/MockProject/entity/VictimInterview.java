package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "victim_interview")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VictimInterview {
    @EmbeddedId
    private VictimInterviewId id;

    @ManyToOne
    @JoinColumn(name="victim_id")
    @MapsId("victimId")
    private Victim victim;

    @ManyToOne
    @JoinColumn(name="interview_id")
    @MapsId("interviewId")
    private Interview interview;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
} 