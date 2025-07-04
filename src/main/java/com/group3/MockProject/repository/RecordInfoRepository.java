package com.group3.MockProject.repository;

import com.group3.MockProject.entity.RecordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordInfoRepository extends JpaRepository<RecordInfo, String> {
}
