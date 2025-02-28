package com.questmast.questmast.core.openai.controller;

import com.questmast.questmast.common.config.ITextService;
import com.questmast.questmast.core.openai.domain.ChatResponse;
import com.questmast.questmast.core.openai.service.ChatGPTApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

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

    @PostMapping("/upload-and-ask")
    public ResponseEntity<String> uploadFileAndAskQuestion(@RequestParam("file") MultipartFile multipartFile) throws IOException, InterruptedException, ExecutionException {
        return ResponseEntity.ok(chatGPTApiService.processPDF(multipartFile));
    }
}
