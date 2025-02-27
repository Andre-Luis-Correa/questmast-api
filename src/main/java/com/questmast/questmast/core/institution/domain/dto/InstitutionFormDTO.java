package com.questmast.questmast.core.institution.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record InstitutionFormDTO(
        @NotBlank
        String name,

        @NotBlank
        String cnpj,

        String siteUrl
) {
}
