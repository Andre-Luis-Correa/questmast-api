package com.questmast.questmast.core.selectionprocesstest.controller;

import com.questmast.questmast.core.boardexaminer.service.BoardExaminerService;
import com.questmast.questmast.core.function.domain.model.Function;
import com.questmast.questmast.core.function.service.FunctionService;
import com.questmast.questmast.core.institution.service.InstitutionService;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import com.questmast.questmast.core.professionallevel.service.ProfessionalLevelService;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.service.SelectionProcessService;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFormDTO;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.selectionprocesstest.service.SelectionProcessTestService;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import com.questmast.questmast.core.testquestioncategory.service.TestQuestionCategoryService;
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
@RequestMapping("/selection-process-test")
public class SelectionProcessTestController {

    private final SelectionProcessTestService selectionProcessTestService;
    private final FunctionService functionService;
    private final ProfessionalLevelService professionalLevelService;
    private final TestQuestionCategoryService testQuestionCategoryService;
    private final SelectionProcessService selectionProcessService;
    private final QuestionService questionService;
    private final InstitutionService institutionService;
    private final BoardExaminerService boardExaminerService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SelectionProcessTestFormDTO selectionProcessTestFormDTO) {
        Function function = functionService.findById(selectionProcessTestFormDTO.functionId());
        ProfessionalLevel professionalLevel = professionalLevelService.findById(selectionProcessTestFormDTO.professionalLevelId());
        TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(selectionProcessTestFormDTO.testQuestionCategoryId());
        SelectionProcess selectionProcess = selectionProcessService.findById(selectionProcessTestFormDTO.selectionProcessId());
        List<Question> questionList = questionService.getValidQuestionList(selectionProcessTestFormDTO.questionList(), selectionProcessTestFormDTO.applicationDate());

        selectionProcessTestService.create(selectionProcessTestFormDTO, function, professionalLevel, testQuestionCategory, selectionProcess, questionList);
        institutionService.addTestsAndQuestionsCounters(selectionProcess.getInstitution(), questionList.size());
        boardExaminerService.addTestsAndQuestionsCounters(selectionProcess.getBoardExaminer(), questionList.size());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody SelectionProcessTestFormDTO selectionProcessTestFormDTO) {
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(id);
        List<Question> oldQuestionList = selectionProcessTest.getQuestionList();
        Function function = functionService.findById(selectionProcessTestFormDTO.functionId());
        ProfessionalLevel professionalLevel = professionalLevelService.findById(selectionProcessTestFormDTO.professionalLevelId());
        TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(selectionProcessTestFormDTO.testQuestionCategoryId());
        SelectionProcess selectionProcess = selectionProcessService.findById(selectionProcessTestFormDTO.selectionProcessId());
        List<Question> questionList = questionService.updateQuestionList(selectionProcessTest.getQuestionList(), selectionProcessTestFormDTO.questionList(), selectionProcessTestFormDTO);

        selectionProcessTestService.update(selectionProcessTestFormDTO, selectionProcessTest, function, professionalLevel, testQuestionCategory, selectionProcess, questionList);
        questionService.deleteUnusedQuestions(oldQuestionList, questionList);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/view-counter/{id}")
    public ResponseEntity<Void> updateViewCounter(@PathVariable Long id) {
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(id);

        selectionProcessTestService.updateViewCounter(selectionProcessTest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(id);
        institutionService.subTestsAndQuestionsCounters(selectionProcessTest.getSelectionProcess().getInstitution(), selectionProcessTest.getQuestionList().size());
        boardExaminerService.subTestsAndQuestionsCounters(selectionProcessTest.getSelectionProcess().getBoardExaminer(), selectionProcessTest.getQuestionList().size());

        selectionProcessTestService.delete(selectionProcessTest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SelectionProcessTest>> list(Pageable pageable) {
        return ResponseEntity.ok().body(selectionProcessTestService.list(pageable));
    }

    @GetMapping
    public ResponseEntity<List<SelectionProcessTest>> list() {
        return ResponseEntity.ok().body(selectionProcessTestService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelectionProcessTest> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(selectionProcessTestService.findById(id));
    }
}
