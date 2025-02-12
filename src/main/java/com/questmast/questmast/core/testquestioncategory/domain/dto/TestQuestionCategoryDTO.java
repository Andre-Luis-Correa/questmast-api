package com.questmast.questmast.core.testquestioncategory.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestQuestionCategoryDTO(

        @NotNull
        Long id,

        @NotBlank
        String name
) {
}
