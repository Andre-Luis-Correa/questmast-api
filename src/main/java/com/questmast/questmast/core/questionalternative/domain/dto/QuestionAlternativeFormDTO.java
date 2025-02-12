package com.questmast.questmast.core.questionalternative.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionAlternativeFormDTO(

        @NotBlank
        String alternativeLetter,

        @NotBlank
        String statement,

        @NotNull
        Boolean isCorrect
) {
}
