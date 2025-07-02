package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "warrant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warrant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "warrant_id", length = 36)
    String warrantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "case_id")
    Case case_id;

    @Column(name = "warrant_name", length = 255)
    String warrantName;

    @Column(name = "attached_file", length = 255)
    String attachedFile;

    @Column(name = "time_pulish")
    LocalDateTime timePulish;

    @Column(name = "is_deleted")
    Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        if (this.isDeleted == null) this.isDeleted = false;
        if (this.timePulish == null) this.timePulish = LocalDateTime.now();
    }
}
