package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "case_evidence")
@IdClass(CaseEvidenceId.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseEnvidence implements Serializable {

    @Id
    @Column(name = "case_id")
    private Long caseId;

    @Id
    @Column(name = "evidence_id")
    private Long evidenceId;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CaseEnvidence that = (CaseEnvidence) o;

        if (caseId != null ? !caseId.equals(that.caseId) : that.caseId != null)
            return false;
        return evidenceId != null ? evidenceId.equals(that.evidenceId) : that.evidenceId == null;
    }

    @Override
    public int hashCode() {
        int result = caseId != null ? caseId.hashCode() : 0;
        result = 31 * result + (evidenceId != null ? evidenceId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Case_Envidence{" +
                "caseId=" + caseId +
                ", evidenceId=" + evidenceId +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
