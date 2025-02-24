package com.questmast.questmast.core.selectionprocesstest.domain.dto;

import com.questmast.questmast.core.question.domain.dto.QuestionUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record SelectionProcessTestUpdateDTO(
        @NotNull
        LocalDate applicationDate,

        @NotBlank
        String name,

        @NotNull
        Long functionId,

        @NotNull
        Long professionalLevelId,

        @NotNull
        Long testQuestionCategoryId,

        @NotNull
        Long selectionProcessId,

        @Valid
        @NotNull
        @NotEmpty
        List<QuestionUpdateDTO> questionList
) {
}
