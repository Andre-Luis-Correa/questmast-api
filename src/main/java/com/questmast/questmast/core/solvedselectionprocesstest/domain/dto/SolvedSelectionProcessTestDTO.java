package com.questmast.questmast.core.solvedselectionprocesstest.domain.dto;

import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record SolvedSelectionProcessTestDTO (
        @NotNull
        Long id,

        @NotNull
        LocalDateTime startDateTime,

        @NotNull
        LocalDateTime endDateTime,

        @NotNull
        Integer quantityOfCorrectAnswers,

        SelectionProcessTest selectionProcessTest,

        @NotNull
        List<SolvedEvaluationTestQuestion> solvedQuestionList
){
}
