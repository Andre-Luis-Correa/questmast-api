package com.questmast.questmast.core.solvedquestionnaire.controller;

import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.questionnaire.domain.model.Questionnaire;
import com.questmast.questmast.core.questionnaire.service.QuestionnaireService;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.solvedevaluationtestquestion.service.SolvedEvaluationTestQuestionService;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireFilterDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireFormDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.model.SolvedQuestionnaire;
import com.questmast.questmast.core.solvedquestionnaire.service.SolvedQuestionnaireService;
import com.questmast.questmast.core.student.domain.model.Student;
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
@RequestMapping("/solved-questionnaire")
public class SolvedQuestionnaireController {

    private final SolvedQuestionnaireService solvedQuestionnaireService;
    private final StudentService studentService;
    private final QuestionService questionService;
    private final QuestionnaireService questionnaireService;
    private final SolvedEvaluationTestQuestionService solvedEvaluationTestQuestionService;

    @PostMapping
    public ResponseEntity<SolvedQuestionnaireDTO> create(@Valid @RequestBody SolvedQuestionnaireFormDTO solvedQuestionnaireFormDTO) {
        Questionnaire questionnaire = questionnaireService.findById(solvedQuestionnaireFormDTO.questionnaireId());
        Student student = studentService.findByMainEmail(solvedQuestionnaireFormDTO.studentMainEmail());
        List<SolvedEvaluationTestQuestion> solvedEvaluationTestQuestionList = solvedEvaluationTestQuestionService.getValidSolvedQuestionList(questionnaire.getQuestionList(), solvedQuestionnaireFormDTO.solvedQuestionList());

        SolvedQuestionnaire solvedQuestionnaire = solvedQuestionnaireService.create(questionnaire, solvedQuestionnaireFormDTO, student, solvedEvaluationTestQuestionList);
        questionService.updateQuestionInformationAfterSolved(solvedQuestionnaire.getSolvedQuestionList());

        return ResponseEntity.status(HttpStatus.CREATED).body(solvedQuestionnaireService.convertToQuestionnaireTestDTO(solvedQuestionnaire));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolvedQuestionnaireDTO> findById(@PathVariable Long id) {
        SolvedQuestionnaire solvedQuestionnaire = solvedQuestionnaireService.findById(id);

        return ResponseEntity.ok().body(solvedQuestionnaireService.convertToQuestionnaireTestDTO(solvedQuestionnaire));
    }

    @GetMapping("/last")
    public ResponseEntity<SolvedQuestionnaireDTO> findLast(SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO) {
        Student student = studentService.findByMainEmailOrNull(solvedQuestionnaireFilterDTO.studentMainEmail());
        Questionnaire questionnaire = questionnaireService.findByIdOrNull(solvedQuestionnaireFilterDTO.questionnaireId());
        SolvedQuestionnaire solvedQuestionnaire = solvedQuestionnaireService.findLastByStudentAndQuestionnaire(student, questionnaire);

        if(solvedQuestionnaire == null) return ResponseEntity.ok(null);

        return ResponseEntity.ok().body(solvedQuestionnaireService.convertToQuestionnaireTestDTO(solvedQuestionnaire));
    }

    @GetMapping
    public ResponseEntity<List<SolvedQuestionnaireDTO>> list(SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO) {
        return ResponseEntity.ok().body(solvedQuestionnaireService.list(solvedQuestionnaireFilterDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SolvedQuestionnaireDTO>> list(Pageable pageable, SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO) {
        return ResponseEntity.ok().body(solvedQuestionnaireService.list(pageable, solvedQuestionnaireFilterDTO));
    }
}
