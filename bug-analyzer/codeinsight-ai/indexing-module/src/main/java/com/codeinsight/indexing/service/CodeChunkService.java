package com.codeinsight.indexing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import com.codeinsight.indexing.entity.CodeChunkEntity;
import com.codeinsight.indexing.repository.CodeChunkRepository;
import com.codeinsight.common.dto.CodeChunkDTO;

@Service
@RequiredArgsConstructor
public class CodeChunkService {

    private final CodeChunkRepository repository;

    public void saveChunks(UUID projectId, List<CodeChunkDTO> dtos) {

        List<CodeChunkEntity> existingEntities = repository.findByProjectId(projectId);
        if(!existingEntities.isEmpty()){
            existingEntities.forEach(repository::delete);
        }
        List<CodeChunkEntity> entities = dtos.stream()
                .map(dto -> CodeChunkEntity.builder()
                        .projectId(projectId)
                        .filePath(dto.getFilePath())
                        .className(dto.getClassName())
                        .methodName(dto.getMethodName())
                        .lineStart(dto.getLineStart())
                        .lineEnd(dto.getLineEnd())
                        .codeText(dto.getCodeText())
                        .build())
                .toList();

        repository.saveAll(entities);
    }

    public List<CodeChunkEntity> getChunk(UUID projectId, String methodName) {
        return repository.findByProjectIdAndMethodName(projectId,methodName);
    }

    public CodeChunkEntity findByFileAndLine(
            UUID projectId,
            String fileName,
            String methodName,
            int lineNumber) {

        return repository
                .findByProjectIdAndFilePathContainingAndMethodNameAndLineStartLessThanEqualAndLineEndGreaterThanEqual(
                        projectId,
                        fileName,
                        methodName,
                        lineNumber,
                        lineNumber
                ).orElse(null);
    }
}