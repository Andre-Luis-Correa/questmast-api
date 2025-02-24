package com.questmast.questmast.core.questionalternative.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionAlternativeUpdateDTO(

        Long id,

        @NotBlank
        String alternativeLetter,

        @NotBlank
        String statement,

        @NotNull
        Boolean isCorrect
) {
}
