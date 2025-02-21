package com.questmast.questmast.core.authentication.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginDTO(

        @NotBlank
        String password,

        @NotBlank
        String mainEmail
) {
}
