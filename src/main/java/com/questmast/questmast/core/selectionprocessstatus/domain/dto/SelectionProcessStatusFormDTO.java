package com.questmast.questmast.core.selectionprocessstatus.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record SelectionProcessStatusFormDTO(

    @NotBlank
    String description
){
}