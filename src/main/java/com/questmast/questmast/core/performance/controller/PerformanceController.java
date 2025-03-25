package com.questmast.questmast.core.performance.controller;

import com.questmast.questmast.core.performance.domain.dto.StudentPerformanceDTO;
import com.questmast.questmast.core.performance.domain.model.Performance;
import com.questmast.questmast.core.performance.service.PerformanceService;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireFilterDTO;
import com.questmast.questmast.core.solvedquestionnaire.service.SolvedQuestionnaireService;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessFilterDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestDTO;
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

        SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO = new SolvedQuestionnaireFilterDTO(null, email);
        List<SolvedQuestionnaireDTO> solvedQuestionnaireDTOList = solvedQuestionnaireService.list(solvedQuestionnaireFilterDTO);

        SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO = new SolvedSelectionProcessFilterDTO(null, email);
        List<SolvedSelectionProcessTestDTO> solvedSelectionProcessTestDTOList = solvedSelectionProcessTestService.list(solvedSelectionProcessFilterDTO);

        StudentPerformanceDTO studentPerformanceDTO = performanceService.getStudentPerformance(student, solvedQuestionnaireDTOList, solvedSelectionProcessTestDTOList);

        return ResponseEntity.ok(studentPerformanceDTO);
    }

    @GetMapping("/{email}/gemini")
    public ResponseEntity<Performance> getPerformanceAnalysis(@PathVariable String email) {
        Student student = studentService.findByMainEmail(email);

        SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO = new SolvedQuestionnaireFilterDTO(null, email);
        List<SolvedQuestionnaireDTO> solvedQuestionnaireDTOList = solvedQuestionnaireService.list(solvedQuestionnaireFilterDTO);

        SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO = new SolvedSelectionProcessFilterDTO(null, email);
        List<SolvedSelectionProcessTestDTO> solvedSelectionProcessTestDTOList = solvedSelectionProcessTestService.list(solvedSelectionProcessFilterDTO);

        StudentPerformanceDTO studentPerformanceDTO = performanceService.getStudentPerformance(student, solvedQuestionnaireDTOList, solvedSelectionProcessTestDTOList);

        return ResponseEntity.ok(performanceService.getPerformanceAiAnalysis(studentPerformanceDTO));
    }
}
