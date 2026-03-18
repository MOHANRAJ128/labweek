package com.codeinsight.project.service;

import com.codeinsight.common.dto.CodeChunkDTO;
import com.codeinsight.indexing.service.CodeChunkService;
import com.codeinsight.parser.service.JavaParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.codeinsight.project.entity.ProjectEntity;
import com.codeinsight.project.repository.ProjectRepository;
import com.codeinsight.project.util.ZipExtractionUtil;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;

    private final JavaParserService parserService;

    private final CodeChunkService codeChunkService;

    @Value("${codeinsight.storage.base-path}")
    private String basePath;

    public UUID uploadProject(String name, MultipartFile file) throws Exception {

        UUID projectId = UUID.randomUUID();

        Path projectDir = Paths.get(basePath, projectId.toString());
        Path extractedDir = projectDir.resolve("extracted");

        Files.createDirectories(projectDir);

        Path zipPath = projectDir.resolve("original.zip");
        Files.copy(file.getInputStream(), zipPath);

        Files.createDirectories(extractedDir);

        ZipExtractionUtil.unzip(zipPath, extractedDir);
        ProjectEntity entity = ProjectEntity.builder()
                .id(projectId)
                .name(name)
                .uploadPath(extractedDir.toString())
                .createdAt(LocalDateTime.now())
                .build();
        Optional<ProjectEntity> existing = repository.findByName(name);
        if(existing.isPresent()){
            entity = existing.get();
            entity.setUploadPath(extractedDir.toString());
            projectId = entity.getId();
        }
        repository.save(entity);

        parserService.parseAndSaveProject(extractedDir.toString(),projectId);

        return projectId;
    }
}