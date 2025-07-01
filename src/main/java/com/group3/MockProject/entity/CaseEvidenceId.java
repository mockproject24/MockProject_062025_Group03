package com.group3.MockProject.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseEvidenceId implements Serializable {

    private Long caseId;
    private Long evidenceId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CaseEvidenceId that = (CaseEvidenceId) o;

        return Objects.equals(caseId, that.caseId) &&
                Objects.equals(evidenceId, that.evidenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseId, evidenceId);
    }

    @Override
    public String toString() {
        return "CaseEvidenceId{" +
                "caseId=" + caseId +
                ", evidenceId=" + evidenceId +
                '}';
    }
}
