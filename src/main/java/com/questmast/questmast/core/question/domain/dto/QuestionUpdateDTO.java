package com.questmast.questmast.core.question.domain.dto;

import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record QuestionUpdateDTO(

        Long id,

        @NotBlank
        String statement,

        @NotBlank
        String explanation,

        @NotBlank
        String videoExplanationUrl,

        @Valid
        @NotNull
        @NotEmpty
        List<QuestionAlternativeUpdateDTO> questionAlternativeList,

        @NotNull
        Long questionDifficultyLevelId,

        @NotNull
        Long subjectId,

        @Valid
        @NotNull
        @NotEmpty
        Set<Long> subjectTopicList,

        @NotNull
        Long testQuestionCategoryId

        ) {
}
