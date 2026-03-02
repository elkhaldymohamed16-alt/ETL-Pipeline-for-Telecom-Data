package com.example.telecometl.repository;

import com.example.telecometl.entity.CallRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRecordRepository extends JpaRepository<CallRecord, Long> {
}