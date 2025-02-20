package com.questmast.questmast.core.address.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpecificAddressFormDTO(
        @NotNull
        Long number,

        @NotBlank
        String complement,

        @NotBlank
        String cep,

        @NotNull
        Long streetId,

        @NotBlank
        String streetTypeAcronym,

        @NotNull
        Long neighborhoodId,

        @NotNull
        Long cityId
) {
}
