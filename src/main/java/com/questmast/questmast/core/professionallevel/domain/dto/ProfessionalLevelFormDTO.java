package com.questmast.questmast.core.professionallevel.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfessionalLevelFormDTO(

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}