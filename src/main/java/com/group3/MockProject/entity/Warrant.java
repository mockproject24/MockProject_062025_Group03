package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "warrants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warrant {

    @Id
    @Column(name = "warrant_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String warrantId;

    @Column(name = "warrant_name")
    String warrantName;

    @Column(name = "attached_file", columnDefinition = "json")
    List<String> attachedFile;

    @Column(name = "time_publish")
    LocalDateTime timePublish;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToMany(mappedBy = "warrant")
    List<WarrantResult> warrantResults;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "warrant")
    List<Evidence> evidences;

    @PrePersist
    protected void onCreate() {
        if (this.timePublish == null) this.timePublish = LocalDateTime.now();
    }
}
