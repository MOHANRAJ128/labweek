package com.codeinsight.ai.controller;

import com.codeinsight.ai.service.AiDebugService;
import com.codeinsight.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiDebugService debugService;

    @GetMapping("/debug/{projectId}/{logId}")
    public String debug(
            @PathVariable UUID projectId,
            @PathVariable UUID logId) {

        return debugService.generatePrompt(projectId, logId);
    }

    @PostMapping("/debug")
    public String debug(
            @RequestParam String projectName,
            @RequestParam("projectFile") MultipartFile projectFile,
            @RequestParam("logFile") MultipartFile logFile) throws Exception {

        return debugService.analyzeAndDebug(projectName,projectFile,logFile);
    }
}