package com.questmast.questmast.core.questiondifficultylevel.controller;

import com.questmast.questmast.core.questiondifficultylevel.domain.dto.QuestionDifficultyLevelDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.dto.QuestionDifficultyLevelFormDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import com.questmast.questmast.core.questiondifficultylevel.service.QuestionDifficultyLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question-difficulty-level")
@RequiredArgsConstructor
public class QuestionDifficultyLevelController {

    private final QuestionDifficultyLevelService questionDifficultyLevelService;

    @PostMapping
    public ResponseEntity<QuestionDifficultyLevelDTO> create(@RequestBody QuestionDifficultyLevelFormDTO questionDifficultyLevelFormDTO) {
        QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.save(questionDifficultyLevelFormDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionDifficultyLevelService.convertEntityToDTO(questionDifficultyLevel));
    }

    @GetMapping
    public ResponseEntity<List<QuestionDifficultyLevelDTO>> listAll() {
        List<QuestionDifficultyLevel> questionDifficultyLevelList = questionDifficultyLevelService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(questionDifficultyLevelService.convertToDTOList(questionDifficultyLevelList));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<QuestionDifficultyLevelDTO>> list(Pageable pageable) {
        Page<QuestionDifficultyLevel> questionDifficultyLevelPage = questionDifficultyLevelService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(questionDifficultyLevelService.convertToDTOPage(questionDifficultyLevelPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDifficultyLevelDTO> update(@PathVariable Long id, @RequestBody QuestionDifficultyLevelFormDTO questionDifficultyLevelFormDTO) {
        QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(questionDifficultyLevelService.update(questionDifficultyLevel, questionDifficultyLevelFormDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDifficultyLevelDTO> findById(@PathVariable Long id) {
        QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(id);
        return ResponseEntity.ok(questionDifficultyLevelService.convertEntityToDTO(questionDifficultyLevel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(id);
        questionDifficultyLevelService.delete(questionDifficultyLevel);
        return ResponseEntity.noContent().build();
    }
}