package com.questmast.questmast.core.subject.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectDTO(

        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}