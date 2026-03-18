package com.codeinsight.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeChunkDTO {

    private String filePath;
    private String className;
    private String methodName;
    private int lineStart;
    private int lineEnd;
    private String codeText;
}