package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersCasesId implements Serializable {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "case_id")
    private String caseId;
} 