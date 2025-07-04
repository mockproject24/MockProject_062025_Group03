package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "victims")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Victim {
    @Id
    @Column(name = "victim_id")
    private String victimId;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "contact")
    private String contact;

    @Column(name = "injuries")
    private String injuries;

    @Column(name = "status")
    private String status;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;
}
