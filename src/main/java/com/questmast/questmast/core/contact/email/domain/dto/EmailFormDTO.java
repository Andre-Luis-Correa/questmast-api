package com.questmast.questmast.core.contact.email.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailFormDTO(
        @NotBlank
        String email
) {
}
