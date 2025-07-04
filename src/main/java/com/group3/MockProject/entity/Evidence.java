package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "evidences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {
        "digitalInvest", "financialInvest", "physicalInvest", "forensicInvest",
        "recordInfos", "caseEntity", "user", "report", "warrant",
        "casesEvidences", "evidencesSuspects", "measureSurveys"
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Evidence {
    @Id
    @EqualsAndHashCode.Include
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
    Set<RecordInfo> recordInfos;

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
    Set<MeasureSurvey> measureSurveys;

    @ManyToOne
    @JoinColumn(name = "warrant_id")
    Warrant warrant;
}
