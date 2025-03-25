package com.questmast.questmast.core.google.controller;

import com.questmast.questmast.common.config.ITextService;
import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.google.service.GoogleStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;
    private final GoogleStorageService googleStorageService;
    private final ITextService iTextService;

    @GetMapping("/image")
    public ResponseEntity<String> getEncodedImage(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(googleStorageService.convertMultipartFileToBase64(file));

    }
    @PostMapping
    public ResponseEntity<String> ask(@RequestParam String prompt) throws IOException, InterruptedException {
        return ResponseEntity.ok(geminiService.sendRequest(prompt));
    }
}
