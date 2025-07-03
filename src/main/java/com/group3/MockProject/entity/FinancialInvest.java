package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "financial_invest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialInvest {
    @Id
    @Column(name = "evidence_id")
    private String evidenceId;

    @Column(name = "summary")
    private String summary;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "evidence_id")
    private Evidence evidence;
}