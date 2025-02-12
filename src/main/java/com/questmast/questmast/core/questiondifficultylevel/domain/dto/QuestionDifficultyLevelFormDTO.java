package com.questmast.questmast.core.questiondifficultylevel.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionDifficultyLevelFormDTO(

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}