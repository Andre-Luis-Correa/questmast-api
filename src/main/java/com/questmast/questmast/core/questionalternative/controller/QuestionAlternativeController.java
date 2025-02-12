package com.questmast.questmast.core.questionalternative.controller;

import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeDTO;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questionalternative.service.QuestionAlternativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question-alternative")
@RequiredArgsConstructor
public class QuestionAlternativeController {

    private final QuestionAlternativeService questionAlternativeService;

    @PostMapping
    public ResponseEntity<QuestionAlternativeDTO> create(@RequestBody QuestionAlternativeFormDTO questionAlternativeFormDTO) {
        QuestionAlternative questionAlternative = questionAlternativeService.save(questionAlternativeFormDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionAlternativeService.convertEntityToDTO(questionAlternative));
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionAlternativeDTO>> listAll() {
        List<QuestionAlternative> questionAlternativeList = questionAlternativeService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(questionAlternativeService.convertToDTOList(questionAlternativeList));
    }

    @GetMapping
    public ResponseEntity<Page<QuestionAlternativeDTO>> list(Pageable pageable) {
        Page<QuestionAlternative> questionAlternativePage = questionAlternativeService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(questionAlternativeService.convertToDTOPage(questionAlternativePage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionAlternativeDTO> update(@PathVariable Long id, @RequestBody QuestionAlternativeFormDTO questionAlternativeFormDTO) {
        QuestionAlternative questionAlternative = questionAlternativeService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(questionAlternativeService.update(questionAlternative, questionAlternativeFormDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionAlternativeDTO> findById(@PathVariable Long id) {
        QuestionAlternative questionAlternative = questionAlternativeService.findById(id);
        return ResponseEntity.ok(questionAlternativeService.convertEntityToDTO(questionAlternative));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        QuestionAlternative questionAlternative = questionAlternativeService.findById(id);
        questionAlternativeService.delete(questionAlternative);
        return ResponseEntity.noContent().build();
    }
}