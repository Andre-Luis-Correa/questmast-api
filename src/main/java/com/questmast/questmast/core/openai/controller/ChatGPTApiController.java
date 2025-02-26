package com.questmast.questmast.core.openai.controller;

import com.questmast.questmast.common.config.ITextService;
import com.questmast.questmast.common.exception.type.ChatGPTApiException;
import com.questmast.questmast.core.openai.service.ChatGPTApiService;
import com.questmast.questmast.core.openai.domain.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/chat-gpt")
public class ChatGPTApiController {

    private final ChatGPTApiService chatGPTApiService;
    private final ITextService iTextService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestParam("prompt") String prompt) {
        return ResponseEntity.ok(chatGPTApiService.chat(prompt));
    }

    @GetMapping
    public ResponseEntity<String> getTextFromPDF(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(iTextService.getTextFromPDF(file));
    }

    @GetMapping("/file-content")
    public ResponseEntity<String> fileContent(@RequestParam("file") MultipartFile multipartFile) {
        String fileId;
        try {
            File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);

            String response = chatGPTApiService.uploadFile(file);
            fileId = chatGPTApiService.extractFileIdFromResponse(response);

            String fileContent = chatGPTApiService.getFileContent(fileId);
            //chatGPTApiService.deleteFile(fileId);

            return ResponseEntity.ok(fileContent);
        } catch (IOException e) {
            log.error("Erro ao processar arquivo: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Erro ao processar o arquivo.");
        }

    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);

            String response = chatGPTApiService.uploadFile(file);

            file.delete();

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Erro ao processar arquivo: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Erro ao processar o arquivo.");
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteFile(@RequestParam String fileId) {
        Boolean isDeleted = chatGPTApiService.deleteFile(fileId);
        return ResponseEntity.ok(isDeleted);
    }

}
