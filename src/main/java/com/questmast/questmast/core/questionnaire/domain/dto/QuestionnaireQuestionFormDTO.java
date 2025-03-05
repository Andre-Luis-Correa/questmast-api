package com.questmast.questmast.core.questionnaire.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionnaireQuestionFormDTO(

        @NotNull
        Long subjectId,

        @NotNull
        List<Long> subjectTopicIds,

        @NotNull
        Integer quantity,

        @NotNull
        Long questionDifficultyLevelId
) {
}
