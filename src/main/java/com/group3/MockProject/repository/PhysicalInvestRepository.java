package com.group3.MockProject.repository;

import com.group3.MockProject.entity.PhysicalInvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalInvestRepository extends JpaRepository<PhysicalInvest, String> {
}
