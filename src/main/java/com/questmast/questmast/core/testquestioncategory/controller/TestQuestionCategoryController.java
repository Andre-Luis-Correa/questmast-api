package com.questmast.questmast.core.testquestioncategory.controller;

import com.questmast.questmast.core.testquestioncategory.domain.dto.TestQuestionCategoryDTO;
import com.questmast.questmast.core.testquestioncategory.domain.dto.TestQuestionCategoryFormDTO;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import com.questmast.questmast.core.testquestioncategory.service.TestQuestionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test-question-category")
@RequiredArgsConstructor
public class TestQuestionCategoryController {

    private final TestQuestionCategoryService testQuestionCategoryService;

    @PostMapping
    public ResponseEntity<TestQuestionCategoryDTO> create(@RequestBody TestQuestionCategoryFormDTO testQuestionCategoryFormDTO) {
        TestQuestionCategory testQuestionCategory = testQuestionCategoryService.save(testQuestionCategoryFormDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(testQuestionCategoryService.convertTestQuestionCategoryToTestQuestionCategoryDTO(testQuestionCategory));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TestQuestionCategoryDTO>> listAll() {
        List<TestQuestionCategory> testQuestionCategoryList = testQuestionCategoryService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(testQuestionCategoryService.convertToTestQuestionCategoryDTOList(testQuestionCategoryList));
    }

    @GetMapping
    public ResponseEntity<Page<TestQuestionCategoryDTO>> list(Pageable pageable) {
        Page<TestQuestionCategory> testQuestionCategoryPage = testQuestionCategoryService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(testQuestionCategoryService.convertToTestQuestionCategoryDTOPage(testQuestionCategoryPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestQuestionCategoryDTO> update(@PathVariable Long id, @RequestBody TestQuestionCategoryFormDTO testQuestionCategoryFormDTO) {
        TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(testQuestionCategoryService.update(testQuestionCategory, testQuestionCategoryFormDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestQuestionCategoryDTO> findById(@PathVariable Long id) {
        TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(id);

        return ResponseEntity.ok(testQuestionCategoryService.convertTestQuestionCategoryToTestQuestionCategoryDTO(testQuestionCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(id);
        testQuestionCategoryService.delete(testQuestionCategory);

        return ResponseEntity.noContent().build();
    }
}