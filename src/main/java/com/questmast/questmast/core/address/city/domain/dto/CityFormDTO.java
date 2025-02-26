package com.questmast.questmast.core.address.city.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CityFormDTO(
        @NotBlank
        String city,

        @NotBlank
        String federateUnit
) {
}
