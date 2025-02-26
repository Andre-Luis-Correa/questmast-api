package com.questmast.questmast.core.address.address.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record SpecificAddressFormDTO(
        @NotBlank
        String number,

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
