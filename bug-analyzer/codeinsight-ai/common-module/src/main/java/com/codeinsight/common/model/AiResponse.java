package com.codeinsight.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiResponse {
    private String projectName;
    private String debuggingResult;
}
