package com.codeinsight.project.controller;

import com.codeinsight.common.dto.CodeChunkDTO;
import com.codeinsight.indexing.entity.CodeChunkEntity;
import com.codeinsight.indexing.repository.CodeChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.codeinsight.project.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    private final CodeChunkRepository codeChunkRepository;

    @PostMapping("/upload")
    public UUID upload(
            @RequestParam String name,
            @RequestParam MultipartFile file) throws Exception {

        return service.uploadProject(name, file);
    }

//    @PostMapping("/analyze/{projectId}")
//    public DebugResponse analyzeError(
//            @PathVariable UUID projectId,
//            @RequestParam("file") MultipartFile errorFile) {
//        return aiService.analyze(projectId, errorFile);
//    }

    @GetMapping("/debug/chunks/{projectId}")
    public List<CodeChunkEntity> getChunks(@PathVariable UUID projectId) {
        String errorLog = """
                NullPointerException at CoinChange.minCoins1(CoinChange.java:23)
                """;
        List<CodeChunkEntity> chunks = codeChunkRepository.findByProjectId(projectId);
        Pattern pattern = Pattern.compile("\\.(\\w+)\\(");
        Matcher matcher = pattern.matcher(errorLog);

        String methodFromError;
        if (matcher.find()) {
            methodFromError = matcher.group(1);
        } else {
            methodFromError = null;
        }
        return chunks.stream()
                .filter(c -> c.getMethodName().equals(methodFromError))
                .collect(Collectors.toList());
    }
}