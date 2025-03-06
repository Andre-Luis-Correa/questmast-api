package com.questmast.questmast.core.google.controller;

import com.questmast.questmast.common.config.ITextService;
import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.google.service.GoogleStorageService;
import com.questmast.questmast.core.question.domain.dto.QuestionFormDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @PostMapping("/pdf")
    public ResponseEntity<List<QuestionFormDTO>> getQuestionsFromPDF(@RequestParam("file") MultipartFile multipartFile) throws IOException, InterruptedException, ExecutionException {
        List<QuestionFormDTO> allQuestions = new ArrayList<>();

        // 1. Converter MultipartFile para PDDocument
        PDDocument document = PDDocument.load(multipartFile.getInputStream());
        int totalPages = document.getNumberOfPages();

        log.info("Total de páginas no PDF: " + totalPages);

        for (int i = 0; i < totalPages; i++) {
            // 2. Criar um novo documento para cada página
            PDDocument singlePageDoc = new PDDocument();
            singlePageDoc.addPage(document.getPage(i));

            // 3. Criar um arquivo temporário para armazenar a página única
            File tempFile = File.createTempFile("pdf_page_" + (i + 1), ".pdf");
            singlePageDoc.save(tempFile);
            singlePageDoc.close();

            // 4. Fazer upload do arquivo e processar o conteúdo
            String fileUri = geminiService.uploadPdfFile(new MockMultipartFile(tempFile.getName(),
                    new FileInputStream(tempFile)));
            log.info("Página " + (i + 1) + " enviada para análise: " + fileUri);

            List<QuestionFormDTO> pageQuestions = geminiService.getPdfFileContent(fileUri);

            // 5. Adicionar perguntas ao resultado final
            allQuestions.addAll(pageQuestions);

            // 6. Excluir o arquivo temporário após o uso
            tempFile.delete();
        }

        document.close();

        return ResponseEntity.ok(allQuestions);
    }

}
