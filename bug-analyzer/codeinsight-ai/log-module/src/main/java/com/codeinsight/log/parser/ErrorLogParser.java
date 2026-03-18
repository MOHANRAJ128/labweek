package com.codeinsight.log.parser;

import com.codeinsight.common.dto.ParsedErrorDto;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ErrorLogParser {

    private static final Pattern STACK_TRACE_PATTERN =
            Pattern.compile("at (\\w+)\\.(\\w+)\\((\\w+)\\.java:(\\d+)\\)");

    public ParsedErrorDto parse(String logContent) {

        Matcher matcher = STACK_TRACE_PATTERN.matcher(logContent);

        if (matcher.find()) {

            String className = matcher.group(1);
            String methodName = matcher.group(2);
            Integer line = Integer.parseInt(matcher.group(4));

            String exception = extractException(logContent);

            return new ParsedErrorDto(
                    exception,
                    className,
                    methodName,
                    line
            );
        }

        return null;
    }

    private String extractException(String logContent) {

        String[] lines = logContent.split("\n");

        for (String line : lines) {

            if (line.contains("Exception")) {
                return line.trim();
            }
        }

        return "UnknownException";
    }
}