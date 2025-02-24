package com.questmast.questmast.core.question.domain.dto;

import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record QuestionFormDTO(

        @NotNull
        LocalDate applicationDate,

        @NotBlank
        String statement,

        @NotBlank
        String explanation,

        @NotBlank
        String videoExplanationUrl,

        @Valid
        @NotNull
        @NotEmpty
        List<QuestionAlternativeFormDTO> questionAlternativeList,

        @NotNull
        Long questionDifficultyLevelId,

        @NotNull
        Long subjectId,

        @Valid
        @NotNull
        @NotEmpty
        List<Long> subjectTopicList,

        @NotNull
        Long testQuestionCategoryId

        ) {
}
