package com.codeinsight.indexing.repository;

import com.codeinsight.indexing.entity.MethodCallEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MethodCallRepository extends JpaRepository<MethodCallEntity, UUID> {
    List<MethodCallEntity> findByProjectIdAndCalleeMethod(UUID projectId, String method);

    List<MethodCallEntity> findByProjectId(UUID projectId);

    List<MethodCallEntity> findByProjectIdAndCallerClass(UUID projectId, String className);
}
