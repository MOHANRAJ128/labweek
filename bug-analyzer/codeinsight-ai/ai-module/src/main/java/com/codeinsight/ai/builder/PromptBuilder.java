package com.codeinsight.ai.builder;

import com.codeinsight.common.dto.ParsedErrorDto;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildPrompt(
            ParsedErrorDto error,
            String callChain, String relatedCode) {

        return """
You are a senior Java debugging expert.

A Java application failed.

Exception:
%s

Class:
%s

Method:
%s

Failing Line Number:
%s

Call Chain:
%s

Related Methods:
%s

Tasks:
1 Identify root cause
2 Trace how incorrect value propagated
3 Suggest fix
4 IMPORTANT: Always respond in STRICT markdown format:
    - Use ### for headings
    - Use ```java for code
    - Use bullet points for explanation

Answer like a senior engineer reviewing a pull request.
"""
                .formatted(
                        error.getExceptionType(),
                        error.getClassName(),
                        error.getMethodName(),
                        error.getLineNumber(),
                        callChain,
                        relatedCode
                );
    }
}

