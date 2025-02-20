package com.questmast.questmast.core.address.address.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpecificAddressFormDTO(
        @NotNull
        Long number,

        @NotBlank
        String complement,

        @NotBlank
        String cep,

        @NotBlank
        String street,

        @NotBlank
        String streetType,

        @NotBlank
        String neighborhood,

        @NotBlank
        String city,

        @NotBlank
        String federateUnit
) {
}
