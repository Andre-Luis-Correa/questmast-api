package com.questmast.questmast.core.selectionprocesstest.domain.dto;

import com.questmast.questmast.core.question.domain.dto.QuestionFormDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SelectionProcessTestFormDTO(

        @NotBlank
        String name,

        @NotNull
        Long functionId,

        @NotNull
        Long professionalLevelId,

        @NotNull
        Long testQuestionCategoryId,

        @NotNull
        Long selectionProcess,

        @Valid
        @NotNull
        @NotEmpty
        List<QuestionFormDTO> questionList
) {
}
