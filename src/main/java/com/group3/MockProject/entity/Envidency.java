package com.group3.MockProject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "envidency")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envidency {
    @Id
    @Column(name = "evidence_id")
    private String evidenceId;

    @Column(name = "measure_unit_id", nullable = false)
    private String measureUnitId;

    @Column(name = "warrant_result_id", nullable = false)
    private String warrantResuiltId;

    @Column(name = "report_id", nullable = false)
    private String reportId;

    @Column(name = "collected_by", nullable = false)
    private String collectedBy;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @Column(name = "current_location", nullable = false)
    private String currentLocation;

    @Column(name = "attached_file", nullable = false)
    private String attachedFile;

    @Column(name = "status")
    private String status;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Override
    public String toString() {
        return "Evidence{" +
                "evidenceId=" + evidenceId +
                ", measureUnitBy='" + measureUnitId + '\'' +
                ", warrantResult='" + warrantResuiltId + '\'' +
                ", reportId=" + reportId +
                ", collectedBy='" + collectedBy + '\'' +
                ", description='" + description + '\'' +
                ", collectedAt=" + collectedAt +
                ", currentLocation='" + currentLocation + '\'' +
                ", attachedFile='" + attachedFile + '\'' +
                ", status='" + status + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
