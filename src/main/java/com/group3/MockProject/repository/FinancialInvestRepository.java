package com.group3.MockProject.repository;

import com.group3.MockProject.entity.FinancialInvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialInvestRepository extends JpaRepository<FinancialInvest, String> {
}
