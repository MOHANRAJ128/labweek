package com.codeinsight.ai.service;

import com.codeinsight.ai.builder.PromptBuilder;
import com.codeinsight.ai.client.Assistant;
import com.codeinsight.indexing.entity.CodeChunkEntity;
import com.codeinsight.indexing.service.CodeChunkService;
import com.codeinsight.indexing.service.CodeContextService;
import com.codeinsight.log.service.LogService;
import com.codeinsight.common.dto.ParsedErrorDto;
import com.codeinsight.parser.service.CallGraphService;
import com.codeinsight.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiDebugService {

    private final LogService logService;
    private final CodeChunkService chunkService;
    private final PromptBuilder promptBuilder;
    private final Assistant aiAssistant;
    private final ProjectService projectService;
    private final CodeHighlighterService codeHighlighter;
    private final CallGraphService callGraphService;
    private final CodeContextService codeContextService;

    public String generatePrompt(UUID projectId, UUID logId) {

        ParsedErrorDto error = logService.getLog(logId);

        StringBuffer callChain = new StringBuffer(error.getClassName()+"."+error.getMethodName());
        List<String> methodChain = callGraphService.buildCallChain(projectId, error.getMethodName(),callChain);

        List<CodeChunkEntity> chunks = codeContextService.getMethods(projectId, methodChain);

        String methodsSection = generateMethodChain(chunks);
//        CodeChunkEntity chunk = chunkService.findByFileAndLine(projectId,error.getClassName(), error.getMethodName(), error.getLineNumber());

//        String highlightedCode =
//                codeHighlighter.highlightFailingLine(
//                        chunk.getCodeText(),
//                        error.getLineNumber());

        String prompt = promptBuilder.buildPrompt(error, callChain.toString(), methodsSection);
        return aiAssistant.chat(prompt);
    }

    private String generateMethodChain(List<CodeChunkEntity> chunks){
        StringBuilder methodsSection = new StringBuilder();

        for (CodeChunkEntity m : chunks) {
            methodsSection.append("Method: ")
                    .append(m.getMethodName())
                    .append("\nCode:\n")
                    .append(m.getCodeText())
                    .append("\n\n");
        }
        return methodsSection.toString();
    }

    public String analyzeAndDebug(String projectName, MultipartFile projectFile, MultipartFile logFile) throws Exception {
        UUID projectId = projectService.uploadProject(projectName,projectFile);
        UUID logId = logService.uploadAndParse(projectId, logFile);
        return generatePrompt(projectId, logId);
    }
}