package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "financial_invest")
public class FinancialInvest {
    @Id
    @Column(name = "evidence_id", nullable = false)
    private String evidenceId;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "evidence_id")
//    private Evidence evidence;
}