package com.codeinsight.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "error_logs")
@Getter
@Setter
public class ErrorLogEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID projectId;

    private String fileName;

    private String filePath;

    private String exceptionType;

    private String className;

    private String methodName;

    private Integer lineNumber;

    @Column(columnDefinition = "TEXT")
    private String rawLog;

    private LocalDateTime createdAt;
}