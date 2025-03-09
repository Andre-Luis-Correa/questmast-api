package com.questmast.questmast.core.solvedquestionnaire.domain.dto;

import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record SolvedQuestionnaireDTO(
        @NotNull
        Long id,

        @NotNull
        LocalDateTime startDateTime,

        @NotNull
        LocalDateTime endDateTime,

        @NotNull
        Integer quantityOfCorrectAnswers,

        @NotNull
        List<SolvedEvaluationTestQuestion> solvedQuestionList
){
}
