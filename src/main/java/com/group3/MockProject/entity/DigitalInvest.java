package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "digital_invest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DigitalInvest {

    @Id
    @Column(name = "evidence_id", length = 36)
    private String evidenceId;

    @Column(name = "device_type", length = 100)
    private String deviceType;

    @Column(name = "analyst_tool", length = 255)
    private String analystTool;

    @Column(name = "result", columnDefinition = "TEXT")
    private String result;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}