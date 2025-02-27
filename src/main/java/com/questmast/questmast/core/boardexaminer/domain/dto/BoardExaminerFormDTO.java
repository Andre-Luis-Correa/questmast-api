package com.questmast.questmast.core.boardexaminer.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record BoardExaminerFormDTO(
        @NotBlank
        String name,

        @NotBlank
        String cnpj,

        String siteUrl

) {
}
