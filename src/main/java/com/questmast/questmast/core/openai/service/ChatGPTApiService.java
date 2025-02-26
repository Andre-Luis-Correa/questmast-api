package com.questmast.questmast.core.openai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.questmast.questmast.common.exception.type.ChatGPTApiException;
import com.questmast.questmast.core.openai.domain.ChatRequest;
import com.questmast.questmast.core.openai.domain.ChatResponse;
import com.questmast.questmast.core.openai.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @Value("${openai.api.url-chat}")
    private String apiChatUrl;

    @Value("${openai.api.url-file}")
    private String apiFileUrl;

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


            chatResponse = restTemplate.postForObject(apiChatUrl, chatRequest, ChatResponse.class);
        } catch (Exception e) {
            log.error("Error Chat GPT: {}", e.getMessage());
            throw new ChatGPTApiException("obter resposta.");
        }

        return chatResponse;
    }


    public String uploadFile(File file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));
            body.add("purpose", "assistants");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiFileUrl, HttpMethod.POST, requestEntity, String.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao fazer upload de arquivo para o ChatGPT: {}", e.getMessage());
            throw new ChatGPTApiException("realizar upload de arquivo.");
        }
    }


    public String extractFileIdFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            return root.get("id").asText();
        } catch (Exception e) {
            throw new ChatGPTApiException("ao extrair o campo file_id da resposta.");
        }
    }

    public Boolean extractFileDeletedStatusFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            return root.get("deleted").asBoolean();
        } catch (Exception e) {
            throw new ChatGPTApiException("ao extrair o campo deleted da resposta.");
        }
    }


    public Boolean deleteFile(String fileId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiFileUrl + "/" + fileId, HttpMethod.DELETE, requestEntity, String.class
            );

            return extractFileDeletedStatusFromResponse(response.getBody());

        } catch (Exception e) {
            log.error("Erro ao fazer upload de arquivo para o ChatGPT: {}", e.getMessage());
            throw new ChatGPTApiException("realizar remoção de arquivo.");
        }
    }

    public String getFileContent(String fileId) {
        try {
            String url = apiFileUrl + "/" + fileId + "/content";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("Error getting content from pdf file: {}", e.getMessage());
            throw new ChatGPTApiException("obter conteúdo do arquivo pdf.");
        }
    }

}
