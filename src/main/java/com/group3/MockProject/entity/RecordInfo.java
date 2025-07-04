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
@Table(name = "records_infos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecordInfo {
    @Id
    @Column(name = "record_info_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String recordInfoId;

    @Column(name = "type_name")
    String typeName;

    @Column(name = "source")
    String source;

    @Column(name = "date_collected")
    LocalDateTime dateCollected;

    @Column(name = "summary")
    String summary;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "evidence_id")
    Evidence evidence;
}
