package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interview")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    @Id
    @Column(name = "interview_id")
    private String interviewId;

    @Column(name = "type_interviewee")
    private String typeInterviewee;

    @Column(name = "location")
    private String location;

    @Column(name = "attached_file")
    private String attachedFile;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    private User interviewer;

    @ManyToOne
    @JoinColumn(name = "investigation_plan_id")
    private InvestigationPlan investigationPlan;

    @OneToMany(mappedBy = "interview")
    private List<Question> questions;

    @OneToMany(mappedBy = "interview")
    private List<WitnessInterview> witnessesInterviews;

    @OneToMany(mappedBy = "interview")
    private List<VictimInterview> victimsInterviews;
} 