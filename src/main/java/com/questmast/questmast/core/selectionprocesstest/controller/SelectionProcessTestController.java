package com.questmast.questmast.core.selectionprocesstest.controller;

import com.questmast.questmast.core.function.Function;
import com.questmast.questmast.core.function.service.FunctionService;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import com.questmast.questmast.core.professionallevel.service.ProfessionalLevelService;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.service.SelectionProcessService;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFormDTO;
import com.questmast.questmast.core.selectionprocesstest.service.SelectionProcessTestService;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import com.questmast.questmast.core.testquestioncategory.service.TestQuestionCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SelectionProcessTestFormDTO selectionProcessTestFormDTO) {
        Function function = functionService.findById(selectionProcessTestFormDTO.functionId());
        ProfessionalLevel professionalLevel = professionalLevelService.findById(selectionProcessTestFormDTO.professionalLevelId());
        TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(selectionProcessTestFormDTO.testQuestionCategoryId());
        SelectionProcess selectionProcess = selectionProcessService.findById(selectionProcessTestFormDTO.selectionProcessId());
        List<Question> questionList = questionService.getValidQuestionList(selectionProcessTestFormDTO.questionList());

        selectionProcessTestService.create(selectionProcessTestFormDTO, function, professionalLevel, testQuestionCategory, selectionProcess, questionList);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
