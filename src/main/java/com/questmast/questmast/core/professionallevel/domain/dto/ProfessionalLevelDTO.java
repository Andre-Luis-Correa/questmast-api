package com.questmast.questmast.core.professionallevel.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfessionalLevelDTO(

        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}