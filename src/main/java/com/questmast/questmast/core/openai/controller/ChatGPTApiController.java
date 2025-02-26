package com.questmast.questmast.core.openai.controller;

import com.questmast.questmast.core.openai.service.ChatGPTApiService;
import com.questmast.questmast.core.openai.domain.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/chat-gpt")
public class ChatGPTApiController {

    private final ChatGPTApiService chatGPTApiService;

    @PostMapping()
    public ResponseEntity<ChatResponse> chat(@RequestParam("prompt") String prompt) {
        return ResponseEntity.ok(chatGPTApiService.chat(prompt));
    }
}
