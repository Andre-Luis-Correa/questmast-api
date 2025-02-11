package com.questmast.questmast.core.selectionprocessstatus.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SelectionProcessStatusDTO(

        @NotNull
        Long id,

        @NotBlank
        String description
) {
}