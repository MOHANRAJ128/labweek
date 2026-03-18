package com.codeinsight.log.service;

import com.codeinsight.common.dto.ParsedErrorDto;
import com.codeinsight.log.entity.ErrorLogEntity;
import com.codeinsight.log.parser.ErrorLogParser;
import com.codeinsight.log.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogService {

    @Value("${codeinsight.storage.base-path}")
    private String basePath;
    private final ErrorLogRepository repository;
    private final ErrorLogParser parser;

    public UUID uploadAndParse(UUID projectId, MultipartFile file) {

        try {

            String content = new String(file.getBytes());

            ParsedErrorDto parsed = parser.parse(content);

            ErrorLogEntity entity = new ErrorLogEntity();
            entity.setProjectId(projectId);

            ErrorLogEntity existing = repository.findByProjectId(projectId);
            if(existing != null){
                entity = existing;
            }

            entity.setFileName(file.getOriginalFilename());
            Path projectDir = Paths.get(basePath, projectId.toString());
            entity.setFilePath(projectDir.toString());
            entity.setRawLog(content);
            entity.setCreatedAt(LocalDateTime.now());

            if (parsed != null) {
                entity.setExceptionType(parsed.getExceptionType());
                entity.setClassName(parsed.getClassName());
                entity.setMethodName(parsed.getMethodName());
                entity.setLineNumber(parsed.getLineNumber());
            }

            repository.save(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new RuntimeException("Failed to process log file", e);
        }
    }

    public ParsedErrorDto getLog(UUID logId) {
        return repository.findById(logId)
                .map(entity -> ParsedErrorDto.builder()
                        .exceptionType(entity.getExceptionType())
                        .className(entity.getClassName())
                        .methodName(entity.getMethodName())
                        .lineNumber(entity.getLineNumber())
                        .build())
                .orElseThrow(() -> new RuntimeException("Log not found"));
    }
}