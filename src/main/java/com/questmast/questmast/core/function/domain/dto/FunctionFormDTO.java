package com.questmast.questmast.core.function.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record FunctionFormDTO(
        @NotBlank
        String name,

        @NotBlank
        String description
) {
}
