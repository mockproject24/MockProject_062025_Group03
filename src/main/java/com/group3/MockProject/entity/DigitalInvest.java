package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "digitals_invests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DigitalInvest {

    @Id
    @Column(name = "evidence_id")
    String evidenceId;

    @Column(name = "device_type")
    String deviceType;

    @Column(name = "analyst_tool")
    String analystTool;

    @Column(name = "result", columnDefinition = "MEDIUMTEXT")
    String result;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "evidence_id")
    Evidence evidence;
}