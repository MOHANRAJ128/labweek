package com.codeinsight.parser.service;

import com.codeinsight.indexing.entity.MethodCallEntity;
import com.codeinsight.indexing.repository.MethodCallRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CallGraphService {

    private final MethodCallRepository repository;

    public CallGraphService(MethodCallRepository repository) {
        this.repository = repository;
    }

    public List<String> buildCallChain(UUID projectId, String errorMethod, StringBuffer sb) {

        List<String> chain = new ArrayList<>();

        trace(projectId, errorMethod, chain, sb);

        return chain;
    }

    private void trace(UUID projectId, String method, List<String> chain, StringBuffer sb) {

        chain.add(method);

        List<MethodCallEntity> parents =
                repository.findByProjectIdAndCalleeMethod(projectId, method);

        for(MethodCallEntity parent : parents) {
            sb.append("\n→ ").append(parent.getCallerClass()).append(".").append(parent.getCallerMethod());
            trace(projectId, parent.getCallerMethod(), chain, sb);

        }
    }
}