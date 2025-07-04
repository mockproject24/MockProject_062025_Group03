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
@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Interview {
    @Id
    @Column(name = "interview_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String interviewId;

    @Column(name = "type_interviewee")
    String typeInterviewee;

    @Column(name = "location")
    String location;

    @Column(name = "attached_file", columnDefinition = "MEDIUMTEXT")
    String attachedFile;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    User interviewer;

    @ManyToOne
    @JoinColumn(name = "investigation_plan_id")
    InvestigationPlan investigationPlan;

    @OneToMany(mappedBy = "interview")
    List<Question> questions;

    @OneToMany(mappedBy = "interview")
    List<WitnessInterview> witnessesInterviews;

    @OneToMany(mappedBy = "interview")
    List<VictimInterview> victimsInterviews;
} 