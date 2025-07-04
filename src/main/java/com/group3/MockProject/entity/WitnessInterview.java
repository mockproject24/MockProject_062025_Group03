package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "witnesses_interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WitnessInterview {
    @EmbeddedId
    WitnessInterviewId id;

    @ManyToOne
    @JoinColumn(name = "witness_id")
    @MapsId("witnessId")
    Witness witness;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    @MapsId("interviewId")
    Interview interview;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
