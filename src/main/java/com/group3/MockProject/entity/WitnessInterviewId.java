package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WitnessInterviewId implements Serializable {
    @Column(name = "witness_id")
    private String witnessId;

    @Column(name = "interview_id")
    private String interviewId;
}