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
public class VictimInterviewId implements Serializable {
    @Column(name = "victim_id")
    private String victimId;

    @Column(name = "interview_id")
    private String interviewId;
} 