package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "witness")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Witness {

    @Id
    @Column(name = "witness_id", length = 36)
    private String witnessId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "case_id")
    private Case case_id;

    @Column(name = "fullname", length = 255)
    private String fullname;

    @Column(name = "contact", length = 255)
    private String contact;

    @Column(name = "statement", columnDefinition = "TEXT")
    private String statement;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}