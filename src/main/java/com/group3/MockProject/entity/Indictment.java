package com.group3.MockProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "indictment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Indictment {
    
    @Id
    @Column(name = "indictment_id", length = 50)
    private String indictmentId;
    
    @Column(name = "prosecution_id", length = 50, nullable = false)
    private String prosecutionId;
    
    @Column(name = "content", length = 2000)
    private String content;
    
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
    
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
    
    // Quan hệ với PROSECUTIONS (n-1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prosecution_id", insertable = false, updatable = false)
    private Prosecution prosecution;
}
