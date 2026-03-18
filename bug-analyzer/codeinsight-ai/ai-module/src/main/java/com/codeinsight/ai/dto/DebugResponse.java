package com.codeinsight.ai.dto;

import lombok.Data;

@Data
public class DebugResponse {
    private String rootCause;
    private String problematicLine;
    private String objectInvolved;
    private String suggestion;
}