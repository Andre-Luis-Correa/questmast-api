package com.questmast.questmast.core.subjecttopic.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectTopicFormDTO(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        Long subjectId
) {
}