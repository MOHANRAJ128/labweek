package com.codeinsight.log.controller;

import com.codeinsight.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @PostMapping("/upload/{projectId}")
    public UUID uploadLog(
            @PathVariable UUID projectId,
            @RequestParam("file") MultipartFile file) {

        return logService.uploadAndParse(projectId, file);
    }
}