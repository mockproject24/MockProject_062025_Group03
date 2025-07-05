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
@Table(name = "evidences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Evidence {
    @Id
    @Column(name = "evidence_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String evidenceId;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

    @Column(name = "collected_at")
    LocalDateTime collectedAt;

    @Column(name = "current_location")
    String currentLocation;

    @Column(name = "attach_file")
    String attachFile;

    @Column(name = "status")
    String status;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToOne(mappedBy = "evidence")
    DigitalInvest digitalInvest;

    @OneToOne(mappedBy = "evidence")
    FinancialInvest financialInvest;

    @OneToOne(mappedBy = "evidence")
    PhysicalInvest physicalInvest;

    @OneToMany(mappedBy = "evidence")
    List<RecordInfo> recordInfos;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne(mappedBy = "evidence")
    ForensicInvest forensicInvest;

    @OneToMany(mappedBy = "evidence")
    List<CaseEvidence> casesEvidences;

    @ManyToOne
    @JoinColumn(name = "report_id")
    Report report;

    @OneToMany(mappedBy = "evidence")
    List<SuspectEvidence> evidencesSuspects;

    @OneToMany(mappedBy = "evidence")
    List<MeasureSurvey> measureSurveys;

    @ManyToOne
    @JoinColumn(name = "warrant_id")
    Warrant warrant;
}
