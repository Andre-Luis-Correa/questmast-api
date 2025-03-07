package com.questmast.questmast.core.solvedevaluationtestquestion.domain.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SolvedEvaluationTestQuestionFormDTO(
        @NotNull
        LocalDateTime startDateTime,

        @NotNull
        LocalDateTime endDateTime,

        @NotNull
        Long selectedAlternativeId,

        @NotNull
        Long questionId
) {
}
