package com.questmast.questmast.core.selectionprocess.domain.dto;

import com.questmast.questmast.core.address.city.domain.dto.CityFormDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SelectionProcessFormDTO(
        @NotBlank
        String name,

        @NotNull
        LocalDate openingDate,

        String url,

        @Valid
        @NotNull
        CityFormDTO cityFormDTO,

        @NotNull
        Long boardExaminerId,

        @NotNull
        Long institutionId,

        @NotBlank
        String contentModeratorEmail,

        @NotNull
        Long selectionProcessStatusId
) {
}
