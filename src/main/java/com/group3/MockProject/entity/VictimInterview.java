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
    @Id
    @Column(name = "victim_id")
    private String victimId;

    @Id
    @Column(name = "interview_id")
    private String interviewId;

    @ManyToOne
    @JoinColumn(name = "victim_id", insertable = false, updatable = false)
    private Victim victim;

    @ManyToOne
    @JoinColumn(name = "interview_id", insertable = false, updatable = false)
    private Interview interview;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
} 