package com.questmast.questmast.core.solvedquestionnaire.domain.dto;

import com.questmast.questmast.core.solvedevaluationtestquestion.domain.dto.SolvedEvaluationTestQuestionFormDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record SolvedQuestionnaireFormDTO(
        @NotNull
        Long questionnaireId,

        @NotNull
        LocalDateTime startDateTime,

        @NotNull
        LocalDateTime endDateTime,

        @NotBlank
        String studentMainEmail,

        @NotNull
        List<SolvedEvaluationTestQuestionFormDTO> solvedQuestionList

) {
}
