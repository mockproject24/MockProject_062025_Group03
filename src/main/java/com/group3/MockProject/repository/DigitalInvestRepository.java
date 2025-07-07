package com.group3.MockProject.repository;

import com.group3.MockProject.entity.DigitalInvest;
import com.group3.MockProject.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigitalInvestRepository extends JpaRepository<DigitalInvest, String> {
    List<DigitalInvest> findByEvidence(Evidence evidence);
}
