package com.group3.MockProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
public class WarrantResult {
    @Id
    private String warrantResultId;

    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String policeResponse;

    @NotNull
    private String location;

    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String notes;

    @NotNull
    private LocalDateTime timeActive;

    @NotNull
    @ColumnDefault("false")
    private Boolean is_deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="warrant_id", referencedColumnName = "warrant_id")
    private Warrant warrant;
}
