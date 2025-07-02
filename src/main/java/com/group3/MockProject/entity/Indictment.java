package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "indictments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indictment {
    @Id
    @Column(name = "indictment_id")
    private String indictmentId;

    @Column(name = "content")
    private String content;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "prosecution_id")
    private Prosecution prosecution;
}
