package com.codeinsight.parser.service;

import com.codeinsight.common.dto.CodeChunkDTO;
import com.codeinsight.indexing.entity.MethodCallEntity;
import com.codeinsight.indexing.repository.MethodCallRepository;
import com.codeinsight.indexing.service.CodeChunkService;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JavaCodeParser {

    private final MethodCallRepository repository;
    private final CodeChunkService codeChunkService;

    public void parse(File file, UUID projectId) throws Exception {

        List<CodeChunkDTO> chunks = new ArrayList<>();
        CompilationUnit cu = StaticJavaParser.parse(file);
        Optional<String> classNameOpt =
                cu.getPrimaryTypeName();
        String className = "";
        if (classNameOpt.isPresent()) className = classNameOpt.get();
        List<MethodCallEntity> existingMethodCalls = repository.findByProjectIdAndCallerClass(projectId,className);

        if(!existingMethodCalls.isEmpty()){
            existingMethodCalls.forEach(repository::delete);
        }
        List<MethodDeclaration> methods =
                cu.findAll(MethodDeclaration.class);
        for(MethodDeclaration method : methods) {

            String caller = method.getNameAsString();

            List<MethodCallExpr> calls =
                    method.findAll(MethodCallExpr.class);

            //method calls in this method
            for (MethodCallExpr call : calls) {

                String callee = call.getNameAsString();

                System.out.println(caller + " -> " + callee);

                MethodCallEntity methodCall = new MethodCallEntity();

                methodCall.setProjectId(projectId);
                methodCall.setCallerMethod(caller);
                methodCall.setCalleeMethod(callee);
                methodCall.setCallerClass(className);

                repository.save(methodCall);
            }
            if (method.getRange().isEmpty()) return;

            int start = method.getRange().get().begin.line;
            int end = method.getRange().get().end.line;

            chunks.add(CodeChunkDTO.builder()
                    .filePath(file.getAbsolutePath())
                    .className(className)
                    .methodName(method.getNameAsString())
                    .lineStart(start)
                    .lineEnd(end)
                    .codeText(method.toString())
                    .build());
        }
        codeChunkService.saveChunks(projectId,chunks);
    }
}