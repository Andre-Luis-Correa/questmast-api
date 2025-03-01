package com.questmast.questmast.core.openai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.questmast.questmast.common.exception.type.ChatGPTApiException;
import com.questmast.questmast.core.openai.domain.ChatRequest;
import com.questmast.questmast.core.openai.domain.ChatResponse;
import com.questmast.questmast.core.openai.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatGPTApiService {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.max-completions}")
    private int maxCompletions;

    @Value("${openai.temperature}")
    private double temperature;

    @Value("${openai.api.url-chat}")
    private String apiChatUrl;

    private static final String BASE_URL = "https://api.openai.com/v1";

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

    public String deleteFile(String fileId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/files/" + fileId)
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            throw new ChatGPTApiException("remover arquivo.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return String.valueOf(jsonResponse.getBoolean("deleted"));
    }

    public String uploadFile(File file) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = RequestBody.create(file, okhttp3.MediaType.parse("application/pdf"));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("purpose", "assistants")
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/files")
                .header("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            throw new ChatGPTApiException("realizar upload do arquivo.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getString("id");
    }

    public String createAssistant(String fileId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("name", "Leitor de PDF");
        json.put("instructions", "You are a specialized PDF reader trained to extract detailed questions from documents. Your primary task is to scan the provided PDF files and return all questions in a structured and comprehensive manner");
        json.put("model", "gpt-3.5-turbo");
        json.put("temperature", 0);
        //json.put("response_format", createJsonFormat());

        JSONArray tools = new JSONArray();
        tools.put(new JSONObject().put("type", "file_search"));
        json.put("tools", tools);

        JSONObject toolResources = new JSONObject();
        JSONObject fileSearch = new JSONObject();
        JSONArray vectorStores = new JSONArray();

        JSONObject vectorStore = new JSONObject();
        vectorStore.put("file_ids", new JSONArray().put(fileId));
        vectorStores.put(vectorStore);

        fileSearch.put("vector_stores", vectorStores);
        toolResources.put("file_search", fileSearch);

        json.put("tool_resources", toolResources);

        RequestBody body = RequestBody.create(json.toString(), okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/assistants")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("OpenAI-Beta", "assistants=v2")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            log.error(responseBody);
            throw new ChatGPTApiException("criar assistente.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getString("id");
    }

    private JSONObject createJsonFormat() {
        JSONObject responseFormat = new JSONObject();
        responseFormat.put("type", "json_schema");

        JSONObject jsonSchema = new JSONObject();
        jsonSchema.put("name", "question_form_dto");
        jsonSchema.put("strict", true);

        JSONObject schema = new JSONObject();
        schema.put("type", "object");

        JSONObject properties = new JSONObject();
        properties.put("id", new JSONObject().put("type", "number"));
        properties.put("name", new JSONObject().put("type", "string"));
        properties.put("statement", new JSONObject().put("type", "string"));
        properties.put("explanation", new JSONObject().put("type", "string"));
        properties.put("videoExplanationUrl", new JSONObject().put("type", "string"));

        // Schema para questionAlternativeList
        JSONObject altItem = new JSONObject();
        altItem.put("type", "object");
        JSONObject altProps = new JSONObject();
        altProps.put("id", new JSONObject().put("type", "number"));
        altProps.put("statement", new JSONObject().put("type", "string"));
        altProps.put("isCorrect", new JSONObject().put("type", "boolean"));
        altItem.put("properties", altProps);
        altItem.put("required", new JSONArray().put("statement").put("isCorrect"));
        altItem.put("additionalProperties", false);

        JSONObject questionAlternativeList = new JSONObject();
        questionAlternativeList.put("type", "array");
        questionAlternativeList.put("items", altItem);
        properties.put("questionAlternativeList", questionAlternativeList);

        properties.put("questionDifficultyLevelId", new JSONObject().put("type", "number"));
        properties.put("subjectId", new JSONObject().put("type", "number"));

        JSONObject subjectTopicList = new JSONObject();
        subjectTopicList.put("type", "array");
        subjectTopicList.put("items", new JSONObject().put("type", "number"));
        properties.put("subjectTopicList", subjectTopicList);

        properties.put("testQuestionCategoryId", new JSONObject().put("type", "number"));

        schema.put("properties", properties);
        schema.put("required", new JSONArray()
                //.put("id")
                .put("name")
                .put("statement")
                .put("explanation")
                //.put("videoExplanationUrl")
                .put("questionAlternativeList"));
                //.put("questionDifficultyLevelId")
                //.put("subjectId")
                //.put("subjectTopicList")
                //.put("testQuestionCategoryId"));
        schema.put("additionalProperties", false);

        jsonSchema.put("schema", schema);
        responseFormat.put("json_schema", jsonSchema);

        return responseFormat;
    }

    public String createThread() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create("{}", okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads")
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            throw new ChatGPTApiException("criar thread.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getString("id");
    }

    public String sendMessage(String threadId, String fileId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("role", "user");
        json.put("content", "Read the PDF file and return question 1, 2, 3, 4, 5, 6, 7 and 8 in detail, including the wording and alternatives.");

        JSONArray attachments = new JSONArray();
        JSONObject attachment = new JSONObject();
        attachment.put("file_id", fileId);

        JSONArray tools = new JSONArray();
        tools.put(new JSONObject().put("type", "file_search"));
        attachment.put("tools", tools);

        attachments.put(attachment);
        json.put("attachments", attachments);

        RequestBody body = RequestBody.create(json.toString(), okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads/" + threadId + "/messages")
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            throw new ChatGPTApiException("enviar mensagem.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getString("id");
    }

    public String runThread(String threadId, String assistantId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("assistant_id", assistantId);

        RequestBody body = RequestBody.create(json.toString(), okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads/" + threadId + "/runs")
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            log.error(responseBody);
            throw new ChatGPTApiException("executar thread.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getString("id");
    }

    public String checkRunStatus(String threadId, String runId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads/" + threadId + "/runs/" + runId)
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        log.info(responseBody);

        if (!response.isSuccessful()) {
            log.error(responseBody);
            throw new ChatGPTApiException("verificar status da execução da thread.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getString("status");
    }

    public String processChatResponse(String threadId, String runId) throws InterruptedException, IOException, ExecutionException {
        int maxAttempts = 30;
        int attempt = 0;
        int waitTimeMillis = 2000;

        AtomicReference<String> runStatus = new AtomicReference<>(checkRunStatus(threadId, runId));

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        while (!runStatus.get().equals("completed") && attempt < maxAttempts) {
            if (runStatus.get().equals("failed") || runStatus.get().equals("cancelled")) {
                log.error(runStatus);
                throw new ChatGPTApiException("processamento da resposta falhou ou foi cancelado.");
            }

            ScheduledFuture<?> future = scheduler.schedule(() -> {
                try {
                    runStatus.set(checkRunStatus(threadId, runId));
                } catch (IOException e) {
                    throw new ChatGPTApiException("processamento da resposta foi interrompido.");
                }
            }, waitTimeMillis, TimeUnit.MILLISECONDS);

            future.get();
            attempt++;
        }

        if (!runStatus.get().equals("completed")) {
            throw new ChatGPTApiException("processamento da resposta demorou além do esperado.");
        }

        return getMessages(threadId);
    }

    public String getMessages(String threadId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads/" + threadId + "/messages")
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            log.error(responseBody);
            throw new ChatGPTApiException("obter mensagens da resposta.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray messages = jsonResponse.getJSONArray("data");

        StringBuilder allMessages = new StringBuilder();
        log.info(messages.length());
        for (int i = 0; i < messages.length(); i++) {
            JSONObject message = messages.getJSONObject(i);
            if (message.has("content")) {
                JSONObject content = message.getJSONArray("content").getJSONObject(0).getJSONObject("text");
                log.info(content.getString("value"));
                allMessages.append(content.getString("value")).append("\n\n");
            }
        }

        return allMessages.toString();
    }

    public String processPDF(MultipartFile multipartFile) throws IOException, InterruptedException, ExecutionException {
        File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);

        String fileId = uploadFile(file);
        log.info("Arquivo enviado com sucesso! file_id: {}", fileId);

        String assistantId = createAssistant(fileId);
        log.info("Assistente criado com sucesso! assistant_id: {}", assistantId);

        String threadId = createThread();
        log.info("Thread criada com sucesso! thread_id: {}", threadId);

        String messageId = sendMessage(threadId, fileId);
        log.info("Mensagem enviada com sucesso! message_id: {}", messageId);

        String runId = runThread(threadId, assistantId);
        log.info("Resposta da IA: {}", runId);

        String response = processChatResponse(threadId, runId);

        deleteFile(fileId);

        return response;
    }

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

    public static String extractFileUriFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonString);

            JsonNode uriNode = rootNode
                    .path("file")
                    .path("uri");

            return uriNode.asText();
        } catch (Exception e) {
            throw new ChatGPTApiException("obter link do arquivo.");
        }
    }

    public String getPdfFileContent(String pdfFileUri) throws IOException, InterruptedException {
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
                                "text": "Extraia as questões desse pdf em forma de json com os campos nome da questão, enunciado, lista de alternativas, alternativa correta."
                            }
                        ]
                    }
                ],
                "generationConfig": {
                    "temperature": 1,
                    "topK": 40,
                    "topP": 0.95,
                    "maxOutputTokens": 8192,
                    "responseMimeType": "text/plain"
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
        return extractTextFromJson(response.body());
    }

    public static String extractTextFromJson(String response) {
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
            throw new ChatGPTApiException("obter resposta.");
        }
    }
}
