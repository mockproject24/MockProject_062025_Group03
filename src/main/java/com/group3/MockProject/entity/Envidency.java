package com.group3.MockProject.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evidences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envidency {
    @Id
    @Column(name = "evidence_id")
    private String evidenceId;

    @Column(name = "description")
    private String description;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @Column(name = "current_location")
    private String currentLocation;

    @Column(name = "attach_file")
    private String attachFile;

    @Column(name = "status")
    private String status;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne(mappedBy = "evidence")
    private PhysicalInvest physicalInvest;

    @OneToMany(mappedBy = "evidence")
    private List<RecordInfo> recordInfos;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "evidence")
    private ForensicInvest forensicInvest;

    @OneToMany(mappedBy = "evidence")
    private List<CaseEnvidence> casesEvidences;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @OneToMany(mappedBy = "evidence")
    private List<WarrantEvidence> evidencesSuspects;

    @ManyToOne
    @JoinColumn(name = "warrant_id")
    private Warrant warrant;

    @Override
    public String toString() {
        return "Evidence{" +
                "evidenceId=" + evidenceId +
                ", description='" + description + '\'' +
                ", collectedAt=" + collectedAt +
                ", currentLocation='" + currentLocation + '\'' +
                ", attachFile='" + attachFile + '\'' +
                ", status='" + status + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
