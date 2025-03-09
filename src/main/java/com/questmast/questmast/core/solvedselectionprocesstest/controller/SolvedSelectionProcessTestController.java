package com.questmast.questmast.core.solvedselectionprocesstest.controller;

import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.selectionprocesstest.service.SelectionProcessTestService;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.solvedevaluationtestquestion.service.SolvedEvaluationTestQuestionService;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessFilterDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestFormDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.model.SolvedSelectionProcessTest;
import com.questmast.questmast.core.solvedselectionprocesstest.service.SolvedSelectionProcessTestService;
import com.questmast.questmast.core.student.domain.Student;
import com.questmast.questmast.core.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/solved-selection-process-test")
public class SolvedSelectionProcessTestController {

    private final SolvedSelectionProcessTestService solvedSelectionProcessTestService;
    private final SelectionProcessTestService selectionProcessTestService;
    private final StudentService studentService;
    private final QuestionService questionService;
    private final SolvedEvaluationTestQuestionService solvedEvaluationTestQuestionService;

    @PostMapping
    public ResponseEntity<SolvedSelectionProcessTestDTO> create(@Valid @RequestBody SolvedSelectionProcessTestFormDTO solvedSelectionProcessTestFormDTO) {
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(solvedSelectionProcessTestFormDTO.selectionProcessTestId());
        Student student = studentService.findByMainEmail(solvedSelectionProcessTestFormDTO.studentMainEmail());
        List<SolvedEvaluationTestQuestion> solvedEvaluationTestQuestionList = solvedEvaluationTestQuestionService.getValidSolvedQuestionList(selectionProcessTest.getQuestionList(), solvedSelectionProcessTestFormDTO.solvedQuestionList());

        SolvedSelectionProcessTest solvedSelectionProcessTest = solvedSelectionProcessTestService.create(selectionProcessTest, solvedSelectionProcessTestFormDTO, student, solvedEvaluationTestQuestionList);
        questionService.updateQuestionInformationAfterSolved(solvedSelectionProcessTest.getSolvedQuestionList());

        return ResponseEntity.status(HttpStatus.CREATED).body(solvedSelectionProcessTestService.convertToSolvedSelectionProcessTestDTO(solvedSelectionProcessTest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolvedSelectionProcessTestDTO> findById(@PathVariable Long id) {
        SolvedSelectionProcessTest solvedSelectionProcessTest = solvedSelectionProcessTestService.findById(id);

        return ResponseEntity.ok().body(solvedSelectionProcessTestService.convertToSolvedSelectionProcessTestDTO(solvedSelectionProcessTest));
    }

    @GetMapping("/last")
    public ResponseEntity<SolvedSelectionProcessTestDTO> findLast(SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO) {
        Student student = studentService.findByMainEmail(solvedSelectionProcessFilterDTO.studentMainEmail());
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(solvedSelectionProcessFilterDTO.selectionProcessTestId());
        SolvedSelectionProcessTest solvedSelectionProcessTest = solvedSelectionProcessTestService.findLastByStudentAndSelectionProcessTest(student, selectionProcessTest);

        return ResponseEntity.ok().body(solvedSelectionProcessTestService.convertToSolvedSelectionProcessTestDTO(solvedSelectionProcessTest));
    }

    @GetMapping
    public ResponseEntity<List<SolvedSelectionProcessTestDTO>> list(SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO) {
        return ResponseEntity.ok().body(solvedSelectionProcessTestService.list(solvedSelectionProcessFilterDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SolvedSelectionProcessTestDTO>> list(Pageable pageable, SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO) {
        return ResponseEntity.ok().body(solvedSelectionProcessTestService.list(pageable, solvedSelectionProcessFilterDTO));
    }
}
