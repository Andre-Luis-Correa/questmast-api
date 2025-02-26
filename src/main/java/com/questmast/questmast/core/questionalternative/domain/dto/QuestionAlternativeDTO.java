package com.questmast.questmast.core.questionalternative.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionAlternativeDTO(

        @NotNull
        Long id,

        @NotBlank
        String statement,

        @NotNull
        Boolean isCorrect
) {
}
