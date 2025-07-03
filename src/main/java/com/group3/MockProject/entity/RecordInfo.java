package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "record_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordInfo {
    @Id
    @Column(name = "record_info_id")
    private String recordInfoId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "source")
    private String source;

    @Column(name = "date_collected")
    private LocalDateTime dateCollected;

    @Column(name = "summary")
    private String summary;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "evidence_id")
    private Evidence evidence;
}
