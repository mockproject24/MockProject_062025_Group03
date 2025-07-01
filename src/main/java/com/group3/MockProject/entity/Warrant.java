package com.group3.MockProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warrant {
    @Id
    private String warrantId;

    @NotNull
    private String warrantName;


    @NotNull
    private String attachedFile;


    @NotNull
    private LocalDateTime timePublish;

    @NotNull
    @ColumnDefault("false")
    private Boolean is_deleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="case_id", referencedColumnName = "case_id")
    private Case caseId;

    @OneToMany
    private Set<WarrantResult> warrantResults;

}
