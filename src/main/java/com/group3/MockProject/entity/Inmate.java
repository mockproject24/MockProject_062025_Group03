package com.group3.MockProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inmate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inmate {
    
    @Id
    @Column(name = "inmate_id", length = 50)
    private String inmateId;
    
    @Column(name = "sentence_id", length = 50, nullable = false)
    private String sentenceId;
    
    @Column(name = "fullname", length = 200, nullable = false)
    private String fullname;
    
    @Column(name = "assigned_facility", length = 200)
    private String assignedFacility;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "expected_release")
    private LocalDate expectedRelease;
    
    @Column(name = "health_status", length = 100)
    private String healthStatus;
    
    @Column(name = "status", length = 50)
    private String status; // Trạng thái: đang thi hành án, tạm tha, hoãn thi hành...
    
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
    
    // Quan hệ với SENTENCES (n-1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id", insertable = false, updatable = false)
    private Sentence sentence;
}