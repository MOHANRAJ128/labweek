package com.codeinsight.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParsedErrorDto {

    private String exceptionType;

    private String className;

    private String methodName;

    private Integer lineNumber;
}