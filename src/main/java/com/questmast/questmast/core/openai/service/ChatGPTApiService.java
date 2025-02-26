package com.questmast.questmast.core.openai.service;

import com.questmast.questmast.common.exception.type.ChatGPTApiException;
import com.questmast.questmast.core.openai.domain.ChatRequest;
import com.questmast.questmast.core.openai.domain.ChatResponse;
import com.questmast.questmast.core.openai.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatGPTApiService {

    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.max-completions}")
    private int maxCompletions;

    @Value("${openai.temperature}")
    private double temperature;

    @Value("${openai.api.url}")
    private String apiUrl;

    public ChatResponse chat(String prompt) {
        ChatResponse chatResponse;
        List<Message> ChatMessages = new ArrayList<>();

        try {
            ChatMessages.add(new Message("user", prompt));

            ChatRequest chatRequest = ChatRequest.builder()
                    .model(model)
                    .messages(ChatMessages)
                    .n(maxCompletions)
                    .temperature(temperature)
                    .build();


            chatResponse = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
        }catch(Exception e) {
            log.error("Error Chat GPT: {}", e.getMessage());
            throw new ChatGPTApiException();
        }

        return chatResponse;
    }
}
