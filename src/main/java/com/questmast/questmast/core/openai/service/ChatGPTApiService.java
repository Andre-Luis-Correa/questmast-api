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
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatGPTApiService {

    private final RestTemplate restTemplate;

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
        json.put("instructions", "Extrair todas as questões que estão no arquivo PDF.");
        json.put("model", "gpt-3.5-turbo");

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
            throw new ChatGPTApiException("criar assistente.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getString("id");
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
        json.put("content", "Extraia todas as questões, sem excessão, incluindo enunciado e alternativas completas..");

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
                .header("OpenAI-Beta", "assistants=v2") // ✅ Cabeçalho correto
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (!response.isSuccessful()) {
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

        if(!response.isSuccessful()) {
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
                throw new ChatGPTApiException("processamento da resposta falhou ou foi cancelado.");
            }

            ScheduledFuture<?> future = scheduler.schedule(() -> {
                try {
                    runStatus.set(checkRunStatus(threadId, runId));
                } catch (IOException e) {
                    throw new ChatGPTApiException("processamento da resposta falhou ou foi cancelado.");
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
            throw new ChatGPTApiException("obter mensagens da resposta.");
        }

        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray messages = jsonResponse.getJSONArray("data");

        // Agora estamos construindo uma string com todas as mensagens, não apenas a primeira
        StringBuilder allMessages = new StringBuilder();
        log.info(messages.length());
        for (int i = 0; i < messages.length(); i++) {
            JSONObject message = messages.getJSONObject(i);
            if (message.has("content")) {
                JSONObject content = message.getJSONArray("content").getJSONObject(0).getJSONObject("text");
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
}
