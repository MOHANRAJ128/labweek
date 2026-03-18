package com.codeinsight.ai.service;

import org.springframework.stereotype.Service;

@Service
public class CodeHighlighterService {

    public String highlightFailingLine(String methodCode, int failingLine) {

        String[] lines = methodCode.split("\n");

        StringBuilder highlighted = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {

            int currentLine = i + 1;

            if (currentLine == failingLine) {
                highlighted.append(">> ERROR LINE >> ")
                        .append(lines[i])
                        .append("\n");
            } else {
                highlighted.append(lines[i]).append("\n");
            }
        }

        return highlighted.toString();
    }
}