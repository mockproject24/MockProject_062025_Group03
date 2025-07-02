package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "digital_inverts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DigitalInvest {
    @Id
    @Column(name = "evidence_id")
    private String evidenceId;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "analyst_tool")
    private String analystTool;

    @Column(name = "result")
    private String result;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "evidence_id")
    @MapsId
    private Envidency evidence;
}