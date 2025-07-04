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
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String eventId;

    @Column(name = "suspect_id")
    String suspectId;

    @Column(name = "case_id")
    String caseId;

    @Column(name = "time_start")
    LocalDateTime timeStart;

    @Column(name = "time_end")
    LocalDateTime timeEnd;

    @Column(name = "event_name")
    String eventName;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}