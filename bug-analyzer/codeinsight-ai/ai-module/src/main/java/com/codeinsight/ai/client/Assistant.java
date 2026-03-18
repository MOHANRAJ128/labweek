package com.codeinsight.ai.client;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {
    @SystemMessage("You are a helpful assistant. You give concise answers.")
    String chat(String userMessage);
}
