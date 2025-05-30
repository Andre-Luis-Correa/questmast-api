package com.questmast.questmast.core.selectionprocesstest.controller;

import com.questmast.questmast.common.exception.type.NotAuthorizedException;
import com.questmast.questmast.core.boardexaminer.service.BoardExaminerService;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.contentmoderator.service.ContentModeratorService;
import com.questmast.questmast.core.function.domain.model.Function;
import com.questmast.questmast.core.function.service.FunctionService;
import com.questmast.questmast.core.institution.service.InstitutionService;
import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import com.questmast.questmast.core.professionallevel.service.ProfessionalLevelService;
import com.questmast.questmast.core.question.domain.dto.QuestionFormDTO;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.service.SelectionProcessService;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFilterDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private final ContentModeratorService contentModeratorService;
    private final GeminiService geminiService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody SelectionProcessTestFormDTO selectionProcessTestFormDTO) {
        ContentModerator contentModerator = contentModeratorService.findByMainEmail(selectionProcessTestFormDTO.contentModeratorEmail());
        SelectionProcess selectionProcess = selectionProcessService.findById(selectionProcessTestFormDTO.selectionProcessId());

        if(!contentModerator.equals(selectionProcess.getContentModerator())) {
            throw new NotAuthorizedException(contentModerator.getMainEmail(), "cadastrar prova do processo seletivo");
        }

        Function function = functionService.findById(selectionProcessTestFormDTO.functionId());
        ProfessionalLevel professionalLevel = professionalLevelService.findById(selectionProcessTestFormDTO.professionalLevelId());
        List<Question> questionList = questionService.getValidQuestionList(selectionProcessTestFormDTO.questionList(), selectionProcessTestFormDTO.applicationDate());

        selectionProcessTestService.create(selectionProcessTestFormDTO, contentModerator, function, professionalLevel, selectionProcess, questionList);
        institutionService.addTestsAndQuestionsCounters(selectionProcess.getInstitution(), questionList.size());
        boardExaminerService.addTestsAndQuestionsCounters(selectionProcess.getBoardExaminer(), questionList.size());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody SelectionProcessTestFormDTO selectionProcessTestFormDTO) {
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(id);
        ContentModerator contentModerator = contentModeratorService.findByMainEmail(selectionProcessTestFormDTO.contentModeratorEmail());

        if(!contentModerator.equals(selectionProcessTest.getContentModerator())) {
            throw new NotAuthorizedException(contentModerator.getMainEmail(), "atualizar prova do processo seletivo");
        }

        List<Question> oldQuestionList = selectionProcessTest.getQuestionList();
        Function function = functionService.findById(selectionProcessTestFormDTO.functionId());
        ProfessionalLevel professionalLevel = professionalLevelService.findById(selectionProcessTestFormDTO.professionalLevelId());
        SelectionProcess selectionProcess = selectionProcessService.findById(selectionProcessTestFormDTO.selectionProcessId());
        List<Question> questionList = questionService.updateQuestionList(selectionProcessTest.getQuestionList(), selectionProcessTestFormDTO.questionList(), selectionProcessTestFormDTO);

        selectionProcessTestService.update(selectionProcessTestFormDTO, selectionProcessTest, function, professionalLevel, selectionProcess, questionList);
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
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam String email) {
        ContentModerator contentModerator = contentModeratorService.findByMainEmail(email);
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(id);

        if(!contentModerator.equals(selectionProcessTest.getContentModerator())) {
            throw new NotAuthorizedException(contentModerator.getMainEmail(), "remover prova do processo seletivo");
        }

        selectionProcessTestService.delete(selectionProcessTest);

        institutionService.subTestsAndQuestionsCounters(selectionProcessTest.getSelectionProcess().getInstitution(), selectionProcessTest.getQuestionList().size());
        boardExaminerService.subTestsAndQuestionsCounters(selectionProcessTest.getSelectionProcess().getBoardExaminer(), selectionProcessTest.getQuestionList().size());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SelectionProcessTest>> list(Pageable pageable, SelectionProcessTestFilterDTO selectionProcessTestFilterDTO) {
        return ResponseEntity.ok().body(selectionProcessTestService.list(pageable, selectionProcessTestFilterDTO));
    }

    @GetMapping
    public ResponseEntity<List<SelectionProcessTest>> list(SelectionProcessTestFilterDTO selectionProcessTestFilterDTO) {
        return ResponseEntity.ok().body(selectionProcessTestService.list(selectionProcessTestFilterDTO));
    }

    @GetMapping("/view")
    public ResponseEntity<List<SelectionProcessTest>> listByMostSeen() {
        return ResponseEntity.ok().body(selectionProcessTestService.listByMostSeen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelectionProcessTest> getById(@PathVariable Long id) {
        SelectionProcessTest selectionProcessTest = selectionProcessTestService.findById(id);
        SelectionProcessTest updatedSelectionProcessTest = selectionProcessTestService.updateViewCounter(selectionProcessTest);

        selectionProcessTestService.insertEncodedImages(new ArrayList<>(List.of(updatedSelectionProcessTest)));

        return ResponseEntity.ok().body(updatedSelectionProcessTest);
    }

    @PostMapping("/gemini")
    public ResponseEntity<SelectionProcessTestFormDTO> getQuestionsFromPDF(@RequestParam("file") MultipartFile multipartFile) throws IOException, InterruptedException, ExecutionException {
        return ResponseEntity.ok(geminiService.getQuestionsFromPdfFile(multipartFile));
    }
}
