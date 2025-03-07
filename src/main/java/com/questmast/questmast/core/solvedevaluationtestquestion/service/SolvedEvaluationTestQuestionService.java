package com.questmast.questmast.core.solvedevaluationtestquestion.service;

import com.questmast.questmast.common.exception.type.FieldNotValidException;
import com.questmast.questmast.common.exception.type.QuestionException;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questionalternative.service.QuestionAlternativeService;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.dto.SolvedEvaluationTestQuestionFormDTO;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestFormDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.model.SolvedSelectionProcessTest;
import com.questmast.questmast.core.student.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolvedEvaluationTestQuestionService {

    private final QuestionService questionService;
    private final QuestionAlternativeService questionAlternativeService;

    public List<SolvedEvaluationTestQuestion> getValidSolvedQuestionList(List<Question> testQuestionList, List<SolvedEvaluationTestQuestionFormDTO> solvedEvaluationTestQuestionFormDTOS) {
        List<SolvedEvaluationTestQuestion> solvedQuestionList = new ArrayList<>();

        for(SolvedEvaluationTestQuestionFormDTO dto : solvedEvaluationTestQuestionFormDTOS) {
            Question question = questionService.findById(dto.questionId());
            if(!testQuestionList.contains(question)) {
                throw new QuestionException("A questão com id " + question.getId() + " não pertence a prova.");
            }

            QuestionAlternative questionAlternative = questionAlternativeService.findById(dto.selectedAlternativeId());
            Boolean isCorrect = verifyCorrectAnswer(question, questionAlternative);

            SolvedEvaluationTestQuestion solvedQuestion = new SolvedEvaluationTestQuestion();

            solvedQuestion.setStartDateTime(dto.startDateTime());
            solvedQuestion.setEndDateTime(dto.endDateTime());
            solvedQuestion.setIsCorrect(isCorrect);
            solvedQuestion.setQuestionAlternative(questionAlternative);
            solvedQuestion.setQuestion(question);

            solvedQuestionList.add(solvedQuestion);
        }

        if(solvedQuestionList.size() != testQuestionList.size()) {
            throw new QuestionException("O número de questões respondidas não coincide com a quantidade de questões da prova.");
        }

        return solvedQuestionList;
    }

    private Boolean verifyCorrectAnswer(Question question, QuestionAlternative selectedQuestionAlternative) {
        for(QuestionAlternative questionAlternative : question.getQuestionAlternativeList()) {
            if(questionAlternative.getIsCorrect().equals(Boolean.TRUE)) {
                return questionAlternative.equals(selectedQuestionAlternative);
            }
        }

        throw new QuestionException("Alternativa com id " + selectedQuestionAlternative.getId() + " não pertence a questão com id " + question.getId() + ".");
    }

    public Integer countCorrectAnswers(List<SolvedEvaluationTestQuestion> solvedEvaluationTestQuestionList) {
        Integer counter = 0;

        for(SolvedEvaluationTestQuestion solvedEvaluationTestQuestion : solvedEvaluationTestQuestionList) {
            if(solvedEvaluationTestQuestion.getIsCorrect().equals(Boolean.TRUE)) {
                counter++;
            }
        }

        return counter;
    }
}
