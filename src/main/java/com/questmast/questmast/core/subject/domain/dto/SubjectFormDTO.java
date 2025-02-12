package com.questmast.questmast.core.subject.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectFormDTO(

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}