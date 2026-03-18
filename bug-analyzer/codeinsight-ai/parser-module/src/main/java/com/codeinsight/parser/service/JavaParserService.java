package com.codeinsight.parser.service;

import com.codeinsight.common.dto.CodeChunkDTO;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.*;
import java.util.*;

@Service
public class JavaParserService {

    @Autowired
    private JavaCodeParser codeParser;

    public void parseAndSaveProject(String rootPath, UUID projectId) throws Exception {

        Files.walk(Paths.get(rootPath))
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        codeParser.parse(path.toFile(), projectId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}