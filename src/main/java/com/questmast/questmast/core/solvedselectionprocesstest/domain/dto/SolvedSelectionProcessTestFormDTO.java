package com.questmast.questmast.core.solvedselectionprocesstest.domain.dto;

import com.questmast.questmast.core.solvedevaluationtestquestion.domain.dto.SolvedEvaluationTestQuestionFormDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record SolvedSelectionProcessTestFormDTO(
        @NotNull
        Long selectionProcessTestId,

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
