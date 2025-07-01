package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "record_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordInfo {

    @Id
    @Column(name = "record_info_id", length = 36)
    private String recordInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evidence_id", referencedColumnName = "evidence_id")
    private Evidence evidenceId;

    @Column(name = "type_name", length = 100)
    private String typeName;

    @Column(name = "source", length = 255)
    private String source;

    @Column(name = "date_collected")
    private LocalDate dateCollected; // Dùng LocalDate nếu chỉ cần ngày

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}
