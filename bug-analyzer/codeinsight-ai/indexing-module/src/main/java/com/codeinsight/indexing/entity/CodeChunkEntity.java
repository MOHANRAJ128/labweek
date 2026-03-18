package com.codeinsight.indexing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "code_chunks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeChunkEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID projectId;

    private String filePath;

    private String className;

    private String methodName;

    private int lineStart;

    private int lineEnd;

    @Column(columnDefinition = "TEXT")
    private String codeText;
}