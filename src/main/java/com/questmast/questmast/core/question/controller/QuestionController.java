package com.questmast.questmast.core.question.controller;

import com.questmast.questmast.core.question.domain.dto.QuestionFilterDTO;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.service.SelectionProcessService;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.selectionprocesstest.service.SelectionProcessTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final SelectionProcessService selectionProcessService;
    private final SelectionProcessTestService selectionProcessTestService;

    @PostMapping
    public ResponseEntity<List<Question>> findAll(@RequestBody QuestionFilterDTO questionFilterDTO) {
        List<Question> questionList = new ArrayList<>();
        List<SelectionProcess> selectionProcesseList = selectionProcessService.findAllByBoardExaminerAndInstitution(questionFilterDTO.boardExaminerIds(), questionFilterDTO.institutionIds());

        if(!selectionProcesseList.isEmpty()) {
            List<SelectionProcessTest> selectionProcessTestList = selectionProcessTestService.findAllBySelectionProcessAndFunction(selectionProcesseList, questionFilterDTO.functionIds());
            questionList = selectionProcessTestList.stream().map(SelectionProcessTest::getQuestionList).flatMap(List::stream).toList();
            questionList = questionService.findAllByQuestionsAndDifficultyLevelAndSubjectAndSubjectTopic(questionList, questionFilterDTO.questionDifficultyLevelIds(), questionFilterDTO.subjectFilterDTOList());
        }

        return ResponseEntity.ok(questionList);
    }
}
