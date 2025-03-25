package com.questmast.questmast.core.performance.controller;

import com.questmast.questmast.core.performance.domain.dto.StudentPerformanceDTO;
import com.questmast.questmast.core.performance.domain.model.Performance;
import com.questmast.questmast.core.performance.service.PerformanceService;
import com.questmast.questmast.core.solvedquestionnaire.domain.model.SolvedQuestionnaire;
import com.questmast.questmast.core.solvedquestionnaire.service.SolvedQuestionnaireService;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.model.SolvedSelectionProcessTest;
import com.questmast.questmast.core.solvedselectionprocesstest.service.SolvedSelectionProcessTestService;
import com.questmast.questmast.core.student.domain.model.Student;
import com.questmast.questmast.core.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/performance")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final StudentService studentService;
    private final SolvedQuestionnaireService solvedQuestionnaireService;
    private final SolvedSelectionProcessTestService solvedSelectionProcessTestService;

    @GetMapping("/{email}")
    public ResponseEntity<StudentPerformanceDTO> getPerformanceHistory(@PathVariable String email) {
        Student student = studentService.findByMainEmail(email);

        List<SolvedQuestionnaire> solvedQuestionnaireList = solvedQuestionnaireService.findAllByStudent(student);

        List<SolvedSelectionProcessTest> solvedSelectionProcessTests = solvedSelectionProcessTestService.findAllByStudent(student);

        StudentPerformanceDTO studentPerformanceDTO = performanceService.getStudentPerformance(student, solvedQuestionnaireList, solvedSelectionProcessTests);

        return ResponseEntity.ok(studentPerformanceDTO);
    }

    @GetMapping("/{email}/gemini-analysis")
    public ResponseEntity<Performance> getPerformanceHistoryAndAnalysis(@PathVariable String email) {
        Student student = studentService.findByMainEmail(email);

        List<SolvedQuestionnaire> solvedQuestionnaireList = solvedQuestionnaireService.findAllByStudent(student);

        List<SolvedSelectionProcessTest> solvedSelectionProcessTests = solvedSelectionProcessTestService.findAllByStudent(student);

        StudentPerformanceDTO studentPerformanceDTO = performanceService.getStudentPerformance(student, solvedQuestionnaireList, solvedSelectionProcessTests);

        return ResponseEntity.ok(performanceService.getPerformanceAiAnalysis(studentPerformanceDTO));
    }

    @GetMapping("/{email}/gemini2")
    public ResponseEntity<Performance> getPerformanceAnalysis(@PathVariable String email) {
        Student student = studentService.findByMainEmail(email);

        List<SolvedQuestionnaire> solvedQuestionnaireList = solvedQuestionnaireService.findAllByStudent(student);

        List<SolvedSelectionProcessTest> solvedSelectionProcessTests = solvedSelectionProcessTestService.findAllByStudent(student);

        StudentPerformanceDTO studentPerformanceDTO = performanceService.getStudentPerformance(student, solvedQuestionnaireList, solvedSelectionProcessTests);

        return ResponseEntity.ok(performanceService.getPerformanceAiAnalysis(studentPerformanceDTO));
    }
}
