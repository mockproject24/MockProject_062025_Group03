package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Suspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SuspectRepository extends JpaRepository<Suspect,String> {

    @Query("""
            SELECT s FROM Suspect s
            WHERE s.caseEntity.caseId = :caseId
            AND (:status IS NULL OR s.status = :status)
            AND (:date IS NULL OR (s.catchTime BETWEEN :startOfDay AND :endOfDay))   
            """)
    Page<Suspect> findByCaseIdAndStatusAndCatchTime(String caseId, String status, LocalDate date, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
}
