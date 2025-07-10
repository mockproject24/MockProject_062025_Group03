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

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_interviewer_id")
    User userInterviewer;

    @ManyToOne
    @JoinColumn(name = "suspect_interviewee_id")
    Suspect suspectInterviewee;

    @ManyToOne
    @JoinColumn(name = "victim_interviewee_id")
    Victim victimInterviewee;

    @ManyToOne
    @JoinColumn(name = "witness_interviewee_id")
    Witness witnessInterviewee;

    @OneToMany(mappedBy = "interview", fetch = FetchType.LAZY)
    List<Question> questions;

    @OneToMany(mappedBy = "interview", fetch = FetchType.LAZY)
    List<InterviewFile> interviewFileList;

    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;
} 