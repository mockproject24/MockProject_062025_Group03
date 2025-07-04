package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "victims_interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VictimInterview {
    @EmbeddedId
    VictimInterviewId id;

    @ManyToOne
    @JoinColumn(name = "victim_id")
    @MapsId("victimId")
    Victim victim;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    @MapsId("interviewId")
    Interview interview;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
} 