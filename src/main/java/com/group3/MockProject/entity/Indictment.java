package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "indictments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Indictment {
    @Id
    @Column(name = "indictment_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String indictmentId;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    String content;

    @Column(name = "issued_at")
    LocalDateTime issuedAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "prosecution_id")
    Prosecution prosecution;
}
