package com.group3.MockProject.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "event_id", nullable = false)
    private String eventId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "suspect_id")
//    private Suspect suspect;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "case_id");
//    private Case caseEntity;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Column(name = "time_end")
    private LocalDateTime timeEnd;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}