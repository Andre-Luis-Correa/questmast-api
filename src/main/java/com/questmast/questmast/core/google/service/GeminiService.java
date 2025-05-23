package com.questmast.questmast.core.google.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.questmast.questmast.common.exception.type.AiApiException;
import com.questmast.questmast.core.performance.domain.dto.StudentPerformanceDTO;
import com.questmast.questmast.core.question.domain.dto.QuestionFormDTO;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFormDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
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

    public List<QuestionFormDTO> getPdfFileContent(String pdfFileUri) throws IOException, InterruptedException {
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
                                "text": "Extract all the questions from the provided PDF and return them in JSON format. The statement field must contain the full question text, including any auxiliary content. The explanation field must be in portuguese and explain the question of the test. If there is an answer key or if the correct alternatives are marked in any way, set the correct alternative in the isCorrect field."
                            }
                        ]
                    }
                ],
                "generationConfig": {
                       "temperature": 2,
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
                                       },
                                       "isCorrect": {
                                         "type": "boolean"
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

    public List<QuestionFormDTO> extractQuestionsFromJson(String responseJson) {
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
                        objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionFormDTO.class)
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

    public List<QuestionFormDTO> generateQuestionsForQuestionnaire(String prompt) throws IOException, InterruptedException {
        String escapedPrompt = StringEscapeUtils.escapeJson(prompt);

        String requestBody = String.format("""
        {
          "contents": [
            {
              "role": "user",
              "parts": [
                {
                  "text": "%s. O campo `statement` deve conter o enunciado da questão. O campo `explanation` deve conter uma breve explicação sobre a resposta correta da questão. Retorne o texto com a acentuação correta."
                }
              ]
            }
          ],
          "generationConfig": {
             "temperature": 2,
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
                       "questionDifficultyLevelId": {
                         "type": "integer"
                       },
                       "questionAlternativeList": {
                         "type": "array",
                         "items": {
                           "type": "object",
                           "properties": {
                             "statement": {
                               "type": "string"
                             },
                             "isCorrect": {
                               "type": "boolean"
                             }
                           },
                           "required": [
                             "statement",
                             "isCorrect"
                           ]
                         }
                       }
                     },
                     "required": [
                       "name",
                       "statement",
                       "explanation",
                       "questionDifficultyLevelId",
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
        """, escapedPrompt);

        // Monta a URL da API Gemini.
        // Supondo que você tenha algo como 'geminiApiKey' em algum lugar dessa classe
        String geminiQuestionUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
                + geminiApiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geminiQuestionUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Resposta da API Gemini: {}", response.body());

        return extractQuestionsFromJson(response.body());
    }

    public SelectionProcessTestFormDTO getQuestionsFromPdfFile(MultipartFile multipartFile)  {
        try {

            List<QuestionFormDTO> allQuestions = new ArrayList<>();

            PDDocument document = PDDocument.load(multipartFile.getInputStream());
            int totalPages = document.getNumberOfPages();

            log.info("Total de páginas no PDF: " + totalPages);

            for (int i = 0; i < totalPages; i += 5) {
                PDDocument batchDocument = new PDDocument();

                for (int j = i; j < i + 5 && j < totalPages; j++) {
                    batchDocument.addPage(document.getPage(j));
                }

                File tempFile = File.createTempFile("pdf_batch_" + (i / 5 + 1), ".pdf");
                batchDocument.save(tempFile);
                batchDocument.close();

                String fileUri = uploadPdfFile(new MockMultipartFile(tempFile.getName(), new FileInputStream(tempFile)));
                log.info("Lote de páginas " + (i + 1) + " a " + Math.min(i + 5, totalPages) + " enviado para análise: " + fileUri);

                List<QuestionFormDTO> batchQuestions = getPdfFileContent(fileUri);

                allQuestions.addAll(batchQuestions);

                tempFile.delete();
            }

            document.close();

            return new SelectionProcessTestFormDTO(null, null, null, null, null, null, allQuestions);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AiApiException("obter questões do arquivo pdf.");
        }
    }

    public String getAiPerformanceAnalysis(StudentPerformanceDTO studentPerformanceDTO) {
        try {
            // 1. Converter DTO para JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String performanceJson = objectMapper.writeValueAsString(studentPerformanceDTO);

            // 2. Escapar o JSON, para evitar conflitos na hora de montar o payload
            String escapedPerformanceJson = StringEscapeUtils.escapeJson(performanceJson);

            // 3. Montar o prompt
            //    Você pode ajustar o texto conforme achar melhor, incluindo mais instruções ou formatações.
            String promptText = """
                Você é um tutor virtual especializado em orientar estudantes com base em dados de desempenho. 
                Abaixo está um objeto JSON contendo diversas estatísticas de um estudante. 
                Por favor, realize uma análise detalhada desses dados e apresente:
                  1) Uma visão geral do desempenho do estudante (tempo médio de resposta, número de avaliações, percentual de acertos).
                  2) Comentários sobre a evolução mês a mês, destacando onde houve mais acertos ou erros.
                  3) Disciplinas de melhor e pior desempenho, com dicas de estudos específicas.
                  4) Recomendações gerais de melhoria (técnicas de estudo, gerenciamento de tempo, etc.).
                Responda em Português e use um tom encorajador como se estivesse conversando com o aluno, a resposta não deve passar de 20 linhas e de ser um texto, não responda em formato json. Se não tiver dados suficientes, ou se o estudante não tiver respondido nada, apenas recomende a ele iniciar os estudos na plataforma.
                Segue o JSON:
                %s
                """.formatted(escapedPerformanceJson);

            // 4. Montar o corpo da requisição para a API do Gemini
            //    Observação: o "responseMimeType" pode ser "application/json", pois estamos extraindo
            //    o texto usando 'extractTextFromJson'. Se preferir outra abordagem, basta adaptar.
            String requestBody = String.format("""
        {
          "contents": [
            {
              "role": "user",
              "parts": [
                {
                  "text": "%s"
                }
              ]
            }
          ],
          "generationConfig": {
             "temperature": 2,
             "topK": 40,
             "topP": 0.95,
             "maxOutputTokens": 8192,
             "responseMimeType": "text/plain"
          }
        }
        """, StringEscapeUtils.escapeJson(promptText));

            // 5. Enviar requisição à API do Gemini
            String geminiQuestionUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(geminiQuestionUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Resposta da API Gemini para análise de desempenho:\n{}", response.body());

            // 6. Extrair o texto da resposta JSON e retornar
            return extractTextFromJson(response.body());

        } catch (Exception e) {
            log.error("Erro ao analisar desempenho do estudante via IA: {}", e.getMessage(), e);
            throw new AiApiException("Erro ao analisar desempenho do estudante.");
        }
    }

}
