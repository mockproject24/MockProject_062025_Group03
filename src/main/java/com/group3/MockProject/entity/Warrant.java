package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "warrant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warrant {

    @Id
    @Column(name = "warrant_id")
    private String warrantId;

    @Column(name = "warrant_name")
    private String warrantName;

    @Column(name = "attached_file", columnDefinition = "json")
    private String[] attachedFile;

    @Column(name = "time_publish")
    private LocalDateTime timePublish;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "warrant")
    private List<WarrantResult> warrantResults;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @OneToMany(mappedBy = "warrant")
    private List<Envidency> evidences;

    @PrePersist
    protected void onCreate() {
        if (this.timePublish == null) this.timePublish = LocalDateTime.now();
    }
}
