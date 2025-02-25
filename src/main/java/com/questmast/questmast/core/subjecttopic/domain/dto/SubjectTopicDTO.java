package com.questmast.questmast.core.subjecttopic.domain.dto;

import com.questmast.questmast.core.subject.domain.dto.SubjectDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectTopicDTO(

        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        SubjectDTO subject
) {
}