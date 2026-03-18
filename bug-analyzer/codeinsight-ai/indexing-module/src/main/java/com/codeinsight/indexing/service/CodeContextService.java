package com.codeinsight.indexing.service;

import com.codeinsight.indexing.entity.CodeChunkEntity;
import com.codeinsight.indexing.repository.CodeChunkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CodeContextService {

    private final CodeChunkRepository chunkRepository;

    public List<CodeChunkEntity> getMethods(
            UUID projectId,
            List<String> methodNames) {

        return chunkRepository.findByProjectIdAndMethodNameIn(
                projectId,
                methodNames
        );
    }
}