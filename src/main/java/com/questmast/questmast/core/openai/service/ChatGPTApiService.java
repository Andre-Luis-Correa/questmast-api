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

//    public String uploadFile(File file) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("file", new FileSystemResource(file));
//            body.add("purpose", "assistants");
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    apiFileUrl, HttpMethod.POST, requestEntity, String.class
//            );
//
//            return extractFileIdFromResponse(response.getBody());
//        } catch (Exception e) {
//            log.error("Erro ao fazer upload de arquivo para o ChatGPT: {}", e.getMessage());
//            throw new ChatGPTApiException("realizar upload de arquivo.");
//        }
//    }


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

        log.info("Resposta da OpenAI ao deletar arquivo: {}", responseBody);

        return responseBody; // Retorna a resposta da API sobre a remoção
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

    private static final String BASE_URL = "https://api.openai.com/v1";


    // Método para fazer upload de um arquivo e obter o file_id
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

        log.info("Resposta da OpenAI ao fazer upload : {}", responseBody);

        if (response.isSuccessful()) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getString("id");
        }

        return null;
    }

    public String createAssistant(String fileId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("name", "Leitor de PDF");
        json.put("instructions", "Responda perguntas com base no conteúdo do PDF.");
        json.put("model", "gpt-4-turbo");

        // Definição da ferramenta file_search
        JSONArray tools = new JSONArray();
        tools.put(new JSONObject().put("type", "file_search"));
        json.put("tools", tools);

        // Definição do tool_resources com vector_stores e file_ids
        JSONObject toolResources = new JSONObject();
        JSONObject fileSearch = new JSONObject();
        JSONArray vectorStores = new JSONArray();

        JSONObject vectorStore = new JSONObject();
        vectorStore.put("file_ids", new JSONArray().put(fileId)); // Associa o arquivo ao assistente
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

        log.info("Resposta da OpenAI ao criar assistente: {}", responseBody);

        if (response.isSuccessful()) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getString("id");
        } else {
            log.error("Erro ao criar assistente: {}", responseBody);
            return null;
        }
    }





    // Método para criar uma thread
    public String createThread() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create("{}", okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/threads")
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2") // ✅ Cabeçalho correto
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        log.info("Resposta da OpenAI ao criar thread: {}", responseBody);

        if (response.isSuccessful()) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getString("id");
        }
        return null;
    }


//    // Método para enviar uma mensagem dentro de uma thread
//    public String sendMessage(String threadId, String fileId, String question) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        JSONObject json = new JSONObject();
//        json.put("role", "user");
//        json.put("content", question);
//
//        // Anexa o arquivo à mensagem (correto para Assistants v2)
//        JSONArray attachments = new JSONArray();
//        attachments.put(new JSONObject().put("file_id", fileId));
//        json.put("attachments", attachments);
//
//        RequestBody body = RequestBody.create(json.toString(), okhttp3.MediaType.parse("application/json"));
//
//        Request request = new Request.Builder()
//                .url(BASE_URL + "/threads/" + threadId + "/messages")
//                .header("Authorization", "Bearer " + apiKey)
//                .header("OpenAI-Beta", "assistants=v2") // ✅ Cabeçalho correto
//                .post(body)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        String responseBody = response.body().string();
//
//        log.info("Resposta da OpenAI ao enviar mensagem: {}", responseBody);
//
//        if (response.isSuccessful()) {
//            JSONObject jsonResponse = new JSONObject(responseBody);
//            return jsonResponse.getString("id");
//        }
//        return null;
//    }

    // Método para enviar uma mensagem dentro de uma thread
    public String sendMessage(String threadId, String fileId, String question) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Criação do JSON da mensagem
        JSONObject json = new JSONObject();
        json.put("role", "user");
        json.put("content", question);

        // Criação da estrutura de attachments com file_id e ferramentas necessárias
        JSONArray attachments = new JSONArray();
        JSONObject attachment = new JSONObject();
        attachment.put("file_id", fileId);

        // A ferramenta file_search precisa ser adicionada dentro dos attachments
        JSONArray tools = new JSONArray();
        tools.put(new JSONObject().put("type", "file_search"));
        attachment.put("tools", tools);

        attachments.put(attachment);
        json.put("attachments", attachments);

        // Corpo da requisição
        RequestBody body = RequestBody.create(json.toString(), okhttp3.MediaType.parse("application/json"));

        // Construção da requisição HTTP
        Request request = new Request.Builder()
                .url(BASE_URL + "/threads/" + threadId + "/messages")
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")  // Cabeçalho necessário para Assistants v2
                .post(body)
                .build();

        // Envio da requisição e processamento da resposta
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        log.info("Resposta da OpenAI ao enviar mensagem: {}", responseBody);

        if (response.isSuccessful()) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getString("id");
        } else {
            log.error("Erro ao enviar mensagem: {}", responseBody);
            return null;
        }
    }


    // Método para executar a thread e obter a resposta
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

        log.info("Resposta da OpenAI ao rodar thread: {}", responseBody);

        if (response.isSuccessful()) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getString("id");
        }
        return null;
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

        log.info("Status do run: {}", responseBody);

        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.getString("status"); // Retorna o status do run
    }

    public String processChatResponse(String threadId, String runId) throws InterruptedException, IOException {
        int maxAttempts = 30; // Limite de tentativas para evitar loop infinito
        int attempt = 0;
        int waitTimeMillis = 2000; // Espera de 2 segundos entre as verificações

        String runStatus = checkRunStatus(threadId, runId);

        // Enquanto o status não for "completed" e não atingirmos o número máximo de tentativas
        while (!runStatus.equals("completed") && attempt < maxAttempts) {
            if (runStatus.equals("failed") || runStatus.equals("cancelled")) {
                log.error("Erro: O processamento falhou ou foi cancelado.");
                return "Erro no processamento.";
            }

            log.info("Run ainda não concluído. Status atual: {}", runStatus);
            Thread.sleep(waitTimeMillis); // Aguarda um tempo antes de checar novamente
            runStatus = checkRunStatus(threadId, runId);
            attempt++;
        }

        // Se passamos do limite de tentativas e o status ainda não é "completed"
        if (!runStatus.equals("completed")) {
            log.error("Timeout: O processamento demorou muito para ser concluído.");
            return "Timeout ao processar.";
        }

        // Agora podemos buscar a mensagem de resposta
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

        log.info("Mensagens na thread: {}", responseBody);

        return responseBody; // Retorna todas as mensagens
    }

}
