package com.questmast.questmast.core.selectionprocess.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SelectionProcessFormDTO(
        @NotBlank
        String name,

        String url,

        @NotNull
        Long cityId,

        @NotNull
        Long boardExaminerId,

        @NotNull
        Long contentModeratorId,

        @NotNull
        Long selectionProcessStatusId
) {
}
