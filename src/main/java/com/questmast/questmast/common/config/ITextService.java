package com.questmast.questmast.common.config;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.questmast.questmast.common.exception.type.PDFException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class ITextService {

    public String getTextFromPDF(MultipartFile file) {
        try {
            PdfReader reader = new PdfReader(file.getInputStream());
            PdfDocument pdfDocument = new PdfDocument(reader);
            StringBuilder text = new StringBuilder();

            for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
                text.append(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i)));
            }

            pdfDocument.close();
            return text.toString();
        } catch (IOException e) {
            log.error("Error reading PDF text: {}", e.getMessage());
            throw new PDFException();
        }
    }
}
