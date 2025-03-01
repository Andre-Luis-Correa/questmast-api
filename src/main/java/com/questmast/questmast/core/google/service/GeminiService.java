package com.questmast.questmast.core.google.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.questmast.questmast.common.exception.type.AiApiException;
import com.questmast.questmast.core.question.domain.dto.QuestionFormGeminiDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String sendRequest(String prompt) throws IOException, InterruptedException {
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInput = objectMapper.writeValueAsString(requestBody);

        String geminiQuestionUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geminiQuestionUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return extractTextFromJson(response.body());
    }

    public String uploadPdfFile(MultipartFile pdfFile) {
        String mimeType = "application/pdf";
        long fileLength = pdfFile.getSize();
        String fileName = pdfFile.getOriginalFilename();

        String geminiUploadUrl = "https://generativelanguage.googleapis.com/upload/v1beta/files?key=" + geminiApiKey;

        try {
            URL url = new URL(geminiUploadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("X-Goog-Upload-Command", "start, upload, finalize");
            connection.setRequestProperty("X-Goog-Upload-Header-Content-Length", String.valueOf(fileLength));
            connection.setRequestProperty("X-Goog-Upload-Header-Content-Type", mimeType);
            connection.setRequestProperty("Content-Type", mimeType);

            String jsonMetadata = String.format("{\"file\": {\"display_name\": \"%s\"}}", fileName);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(jsonMetadata.getBytes());
                pdfFile.getInputStream().transferTo(outputStream);
                outputStream.flush();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            log.info(response);

            return extractFileUriFromJson(response.toString());

        } catch (Exception e) {
            return null;
        }
    }

    public  String extractFileUriFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonString);

            JsonNode uriNode = rootNode
                    .path("file")
                    .path("uri");

            return uriNode.asText();
        } catch (Exception e) {
            throw new AiApiException("obter link do arquivo.");
        }
    }

    public List<QuestionFormGeminiDTO> getPdfFileContent(String pdfFileUri) throws IOException, InterruptedException {
        String requestBody = String.format("""
            {
                "contents": [
                    {
                        "role": "user",
                        "parts": [
                            {
                                "fileData": {
                                    "mimeType": "application/pdf",
                                    "fileUri": "%s"
                                }
                            },
                            {
                                "text": "Extract all questions from the provided PDF and return them in JSON format. The `statement` field must contain the full question text, including any auxiliary content. The `explanation` field must be in portuguese and explain the question of the test"
                            }
                        ]
                    }
                ],
                "generationConfig": {
                       "temperature": 1,
                       "topK": 40,
                       "topP": 0.95,
                       "maxOutputTokens": 8192,
                       "responseMimeType": "application/json",
                       "responseSchema": {
                         "type": "object",
                         "properties": {
                           "questionList": {
                             "type": "array",
                             "items": {
                               "type": "object",
                               "properties": {
                                 "name": {
                                   "type": "string"
                                 },
                                 "statement": {
                                   "type": "string"
                                 },
                                 "explanation": {
                                   "type": "string"
                                 },
                                 "questionAlternativeList": {
                                   "type": "array",
                                   "items": {
                                     "type": "object",
                                     "properties": {
                                       "statement": {
                                         "type": "string"
                                       }
                                     },
                                     "required": [
                                       "statement"
                                     ]
                                   }
                                 }
                               },
                               "required": [
                                 "name",
                                 "statement",
                                 "explanation",
                                 "questionAlternativeList"
                               ]
                             }
                           }
                         },
                         "required": [
                           "questionList"
                         ]
                       }
                     }
            }
            """, pdfFileUri);

        String geminiQuestionUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geminiQuestionUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info(response.body());
        
        return extractQuestionsFromJson(response.body());
    }

    public List<QuestionFormGeminiDTO> extractQuestionsFromJson(String responseJson) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(responseJson);

            JsonNode candidatesNode = rootNode.path("candidates");
            if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                JsonNode textNode = candidatesNode.get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text");

                JsonNode extractedJson = objectMapper.readTree(textNode.asText());

                return objectMapper.readValue(
                        extractedJson.path("questionList").toString(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionFormGeminiDTO.class)
                );
            }

        } catch (Exception e) {
            throw new AiApiException("Erro ao converter JSON para DTOs de questões.");
        }

        return List.of();
    }

    public  String extractTextFromJson(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode textNode = rootNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");
            return textNode.asText();
        } catch (Exception e) {
            throw new AiApiException("obter resposta.");
        }
    }
}
