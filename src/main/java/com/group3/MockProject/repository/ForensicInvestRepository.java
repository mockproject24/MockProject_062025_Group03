package com.group3.MockProject.repository;

import com.group3.MockProject.entity.ForensicInvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForensicInvestRepository extends JpaRepository<ForensicInvest, String> {
}
