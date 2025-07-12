package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * VictimRepository
 * <p>
 * Repository for Victim entity operations.
 * Provides data access methods for victim management and identification.
 * <p>
 * Version 1.0
 * Date: 7/5/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 5/7/2025      User         Create
 * 12/7/2025     FongFox      Update
 */
@Repository
public interface VictimRepository extends JpaRepository<Victim, String> {
    Optional<Victim> findVictimByFullname(String fullname);

    /**
     * Finds victim by ID card number
     * Used for interview participant verification
     */
    Optional<Victim> findByVictimIdCard(Long victimIdCard);
}
