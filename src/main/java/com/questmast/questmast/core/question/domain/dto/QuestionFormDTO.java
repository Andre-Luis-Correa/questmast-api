package com.questmast.questmast.core.question.domain.dto;

import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record QuestionFormDTO(

        Long id,

        @NotBlank
        String name,

        String statementImage,

        @NotBlank
        String statement,

        @NotBlank
        String explanation,

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
        Set<Long> subjectTopicList
        ) {
}
