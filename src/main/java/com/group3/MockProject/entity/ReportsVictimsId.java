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
public class ReportsVictimsId implements Serializable {
    @Column(name = "report_id")
    private String reportId;

    @Column(name = "victim_id")
    private String victimId;
} 