package com.questmast.questmast.core.testquestioncategory.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record TestQuestionCategoryFormDTO(

        @NotBlank
        String name
) {
}
