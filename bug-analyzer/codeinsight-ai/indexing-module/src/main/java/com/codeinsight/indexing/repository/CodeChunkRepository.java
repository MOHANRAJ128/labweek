package com.codeinsight.indexing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.codeinsight.indexing.entity.CodeChunkEntity;

public interface CodeChunkRepository
        extends JpaRepository<CodeChunkEntity, UUID> {

    List<CodeChunkEntity> findByProjectId(UUID projectId);

    Optional<CodeChunkEntity>
    findByProjectIdAndFilePathContainingAndLineStartLessThanEqualAndLineEndGreaterThanEqual(
            UUID projectId,
            String filePath,
            int lineStart,
            int lineEnd
    );

    List<CodeChunkEntity> findByProjectIdAndMethodName(UUID projectId, String methodName);

    Optional<CodeChunkEntity> findByProjectIdAndFilePathContainingAndMethodNameAndLineStartLessThanEqualAndLineEndGreaterThanEqual(UUID projectId, String fileName,String methodName, int lineNumber, int lineNumber1);

    List<CodeChunkEntity> findByProjectIdAndMethodNameIn(UUID projectId, List<String> methodNames);
}