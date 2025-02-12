package com.questmast.questmast.core.subject.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectFormDTO(

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}