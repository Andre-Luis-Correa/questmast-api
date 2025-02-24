package com.questmast.questmast.core.selectionprocess.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SelectionProcessFormDTO(
        @NotBlank
        String name,

        @NotNull
        LocalDate date,

        String url,

        @NotNull
        Long cityId,

        @NotNull
        Long boardExaminerId,

        @NotNull
        Long institutionId,

        @NotNull
        Long contentModeratorId,

        @NotNull
        Long selectionProcessStatusId
) {
}
