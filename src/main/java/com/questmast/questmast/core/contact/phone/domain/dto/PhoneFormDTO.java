package com.questmast.questmast.core.contact.phone.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PhoneFormDTO(
        @NotBlank
        String number,

        @NotNull
        Integer dddNumber,

        @NotNull
        Integer ddiNumber
) {
}
