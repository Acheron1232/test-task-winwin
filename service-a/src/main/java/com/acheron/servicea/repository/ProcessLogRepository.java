package com.acheron.servicea.repository;

import com.acheron.servicea.entity.ProcessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessLogRepository extends JpaRepository<ProcessLog, UUID> {

}
