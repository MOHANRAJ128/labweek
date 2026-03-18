package com.codeinsight.log.repository;

import com.codeinsight.log.entity.ErrorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, UUID> {
    ErrorLogEntity findByProjectId(UUID projectId);
}